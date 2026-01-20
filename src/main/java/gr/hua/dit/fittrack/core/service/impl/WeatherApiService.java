package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.config.WeatherProperties;
import gr.hua.dit.fittrack.core.model.WeatherResponse;
import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.web.dto.OpenWeatherForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Comparator;
import java.util.Map;
import java.nio.charset.StandardCharsets;

@Service
public class WeatherApiService implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    // Τα παλιά paths τα αφήνω για συμβατότητα, αλλά ΔΕΝ τα χρησιμοποιούμε πια με OpenWeather.
    private static final String AUTHENTICATION_PATH = "/oauth/token";
    private static final String SOMETHING_PATH = "/something";

    private final RestTemplate restTemplate;
    private final WeatherProperties weatherProperties;

    public WeatherApiService(final RestTemplate restTemplate, final WeatherProperties weatherProperties) {
        if (restTemplate == null) throw new NullPointerException();
        if (weatherProperties == null) throw new NullPointerException();
        this.restTemplate = restTemplate;
        this.weatherProperties = weatherProperties;
    }

    /**
     * OpenWeather δεν χρησιμοποιεί OAuth token, αλλά κρατάμε τη μέθοδο λόγω interface/εργασίας.
     */
    @SuppressWarnings("rawtypes")
    @Cacheable("weatherAccessToken")
    public String getAccessToken() {
        LOGGER.info("OpenWeather: access token not required (returning mock)");
        return "not-needed-for-openweather";
    }

    /**
     * ΠΡΑΓΜΑΤΙΚΟ external call σε OpenWeatherMap (forecast).
     *
     * baseUrl: https://api.openweathermap.org/data/2.5
     * endpoint: /forecast?q={location}&appid={apiKey}&units=metric
     *
     * Επιλέγουμε τον forecast χρόνο που είναι πιο κοντά στο dateTime.
     */
    @Override
    public WeatherResponse getWeatherFor(final LocalDateTime dateTime, final String location) {
        if (dateTime == null) throw new NullPointerException();
        if (location == null) throw new NullPointerException();
        if (location.isBlank()) throw new IllegalArgumentException();

        final String apiKey = this.weatherProperties.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing weather.apiKey (OpenWeather API key)");
        }

        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl()) // π.χ. https://api.openweathermap.org/data/2.5
                .path("/forecast")
                .queryParam("q", location)      // π.χ. "Athens,GR"
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        try {
            final ResponseEntity<OpenWeatherForecastResponse> response =
                    this.restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            OpenWeatherForecastResponse.class
                    );

            if (!response.getStatusCode().is2xxSuccessful()
                    || response.getBody() == null
                    || response.getBody().getList() == null
                    || response.getBody().getList().isEmpty()) {
                LOGGER.warn("OpenWeather returned empty response for location={}", location);
                return null;
            }

            // target time στο timezone μας
            final ZoneId zone = ZoneId.of("Europe/Athens");
            final Instant target = dateTime.atZone(zone).toInstant();

            final OpenWeatherForecastResponse.Item nearest = response.getBody().getList().stream()
                    .min(Comparator.comparingLong(it -> Math.abs(it.getDt() - target.getEpochSecond())))
                    .orElse(null);

            if (nearest == null || nearest.getWeather() == null || nearest.getWeather().isEmpty()) {
                LOGGER.warn("OpenWeather cannot match nearest forecast item for location={}", location);
                return null;
            }

            final String main = nearest.getWeather().get(0).getMain();           // Rain/Clear/Clouds
            final String description = nearest.getWeather().get(0).getDescription();
            final double temp = (nearest.getMain() != null) ? nearest.getMain().getTemp() : Double.NaN;

            WeatherResponse wr = new WeatherResponse();
            wr.setLocation(location);
            wr.setDateTime(dateTime);
            wr.setTemperatureC(Double.isNaN(temp) ? null : temp);

            String summary = (main != null ? main : "Weather");
            if (description != null && !description.isBlank()) summary += " (" + description + ")";
            if (!Double.isNaN(temp)) summary += " - " + temp + "°C";
            wr.setSummary(summary);

            LOGGER.info("OpenWeather matched: location={}, summary={}", location, summary);
            return wr;

        } catch (Exception e) {
            LOGGER.error("OpenWeather call failed for location={}: {}", location, e.getMessage(), e);
            return null;
        }
    }

    /**
     * OpenWeather δεν υποστηρίζει POST "message" endpoint.
     * Το κρατάμε για την εργασία ως stub.
     */
    @Override
    public String postSomethingToWeatherApi(final String message) {
        if (message == null) throw new NullPointerException();
        if (message.isBlank()) throw new IllegalArgumentException();
        return "NOT_SUPPORTED_BY_OPENWEATHER";
    }

    /**
     * “Secured” version: OpenWeather δεν έχει bearer token auth,
     * οπότε το κάνουμε ίδιο με getWeatherFor().
     */
    @Override
    public WeatherResponse getWeatherForSecured(final LocalDateTime dateTime, final String location) {
        return getWeatherFor(dateTime, location);
    }
}
