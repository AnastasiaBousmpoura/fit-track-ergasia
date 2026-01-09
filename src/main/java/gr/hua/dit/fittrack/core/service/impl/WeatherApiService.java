package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.config.WeatherProperties;
import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.core.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

/**
 * REST-based implementation of {@link WeatherService}.
 * (Professor-style, similar to RouteeSmsService)
 */

public class WeatherApiService implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    // Αν το API σου έχει OAuth token endpoint (Routee-like), το βάζεις εδώ:
    private static final String AUTHENTICATION_PATH = "/oauth/token";

    // Endpoints του weather API (προσαρμόζεις)
    private static final String WEATHER_PATH = "/weather";
    private static final String WEATHER_SECURED_PATH = "/weather/secured";
    private static final String SOMETHING_PATH = "/something";

    private final RestTemplate restTemplate;
    private final WeatherProperties weatherProperties;

    public WeatherApiService(final RestTemplate restTemplate, final WeatherProperties weatherProperties) {
        if (restTemplate == null) throw new NullPointerException();
        if (weatherProperties == null) throw new NullPointerException();
        this.restTemplate = restTemplate;
        this.weatherProperties = weatherProperties;
    }

    // --------------------------------------------------
    // Secured token acquisition (optional, Routee-like)
    // --------------------------------------------------

    @SuppressWarnings("rawtypes")
    @Cacheable("weatherAccessToken")
    public String getAccessToken() {
        LOGGER.info("Requesting Weather API Access Token");

        // Αν δεν έχεις OAuth στο weather API, μπορείς να αφήσεις mock token.
        final String appId = this.weatherProperties.getAppId();
        final String appSecret = this.weatherProperties.getAppSecret();

        if (appId == null || appId.isBlank() || appSecret == null || appSecret.isBlank()) {
            LOGGER.warn("Weather API appId/appSecret missing -> returning mock token");
            return "mock-token";
        }

        final String credentials = appId + ":" + appSecret;
        final String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl())
                .path(AUTHENTICATION_PATH)
                .toUriString();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + encoded);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", httpHeaders);
        final ResponseEntity<Map> response =
                this.restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("Weather API token request failed");
        }

        return (String) response.getBody().get("access_token");
    }

    // --------------------------------------------------
    // GET (public)
    // --------------------------------------------------

    @Override
    public WeatherResponse getWeatherFor(final LocalDateTime dateTime, final String location) {
        if (dateTime == null) throw new NullPointerException();
        if (location == null) throw new NullPointerException();
        if (location.isBlank()) throw new IllegalArgumentException();

        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl())
                .path(WEATHER_PATH)
                .queryParam("dateTime", dateTime.toString())
                .queryParam("location", location)
                .toUriString();

        final HttpHeaders httpHeaders = new HttpHeaders();
        if (this.weatherProperties.getApiKey() != null && !this.weatherProperties.getApiKey().isBlank()) {
            httpHeaders.set("X-Api-Key", this.weatherProperties.getApiKey());
        }

        final HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        final ResponseEntity<WeatherResponse> response =
                this.restTemplate.exchange(url, HttpMethod.GET, entity, WeatherResponse.class);

        LOGGER.info("Weather response: {}", response);

        return response.getBody();
    }

    // --------------------------------------------------
    // POST (mock)
    // --------------------------------------------------

    @Override
    public String postSomethingToWeatherApi(final String message) {
        if (message == null) throw new NullPointerException();
        if (message.isBlank()) throw new IllegalArgumentException();

        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl())
                .path(SOMETHING_PATH)
                .toUriString();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (this.weatherProperties.getApiKey() != null && !this.weatherProperties.getApiKey().isBlank()) {
            httpHeaders.set("X-Api-Key", this.weatherProperties.getApiKey());
        }

        final Map<String, Object> body = Map.of("message", message);
        final HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        final ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);
        LOGGER.info("Weather POST response: {}", response);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Weather POST failed");
            return "FAILED";
        }

        return response.getBody();
    }

    // --------------------------------------------------
    // GET (secured) with Authorization: Bearer <token>
    // --------------------------------------------------

    @Override
    public WeatherResponse getWeatherForSecured(final LocalDateTime dateTime, final String location) {
        if (dateTime == null) throw new NullPointerException();
        if (location == null) throw new NullPointerException();
        if (location.isBlank()) throw new IllegalArgumentException();

        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl())
                .path(WEATHER_SECURED_PATH)
                .queryParam("dateTime", dateTime.toString())
                .queryParam("location", location)
                .toUriString();

        final String token = this.getAccessToken(); // cached / mock if missing credentials

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);

        final HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        final ResponseEntity<WeatherResponse> response =
                this.restTemplate.exchange(url, HttpMethod.GET, entity, WeatherResponse.class);

        LOGGER.info("Weather secured response: {}", response);

        return response.getBody();
    }
}
