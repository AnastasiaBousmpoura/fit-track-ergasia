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

/**
 * Υλοποίηση του WeatherService που χρησιμοποιεί ΠΡΑΓΜΑΤΙΚΟ
 * εξωτερικό API (OpenWeatherMap).
 *
 * Ρόλος κλάσης:
 * - Κάνει HTTP κλήσεις στο OpenWeather API
 * - Παίρνει δεδομένα πρόγνωσης (forecast)
 * - Επιλέγει τον καιρό που είναι πιο κοντά στην ώρα του ραντεβού
 * - Επιστρέφει ένα απλό WeatherResponse στο σύστημα
 */
@Service // Δηλώνει Spring Bean
public class WeatherApiService implements WeatherService {

    // Logger για καταγραφή πληροφοριών & σφαλμάτων
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);

    // Παλιά endpoints (OAuth / POST) – δεν χρησιμοποιούνται με OpenWeather,
    // αλλά διατηρούνται για συμβατότητα με το interface της εργασίας
    private static final String AUTHENTICATION_PATH = "/oauth/token";
    private static final String SOMETHING_PATH = "/something";

    // Client για HTTP requests
    private final RestTemplate restTemplate;

    // Ρυθμίσεις weather (baseUrl, apiKey)
    private final WeatherProperties weatherProperties;

    /**
     * Constructor injection (καλή πρακτική στο Spring)
     */
    public WeatherApiService(final RestTemplate restTemplate,
                             final WeatherProperties weatherProperties) {

        // Απλοί έλεγχοι ασφαλείας
        if (restTemplate == null) throw new NullPointerException();
        if (weatherProperties == null) throw new NullPointerException();

        this.restTemplate = restTemplate;
        this.weatherProperties = weatherProperties;
    }

    /**
     * Μέθοδος για OAuth access token.
     *
     * Το OpenWeather ΔΕΝ χρησιμοποιεί OAuth,
     * αλλά η μέθοδος υπάρχει λόγω του interface και της εργασίας.
     * Επιστρέφει dummy τιμή.
     */
    @SuppressWarnings("rawtypes")
    @Cacheable("weatherAccessToken")
    public String getAccessToken() {
        LOGGER.info("OpenWeather: access token not required (returning mock)");
        return "not-needed-for-openweather";
    }

    /**
     * Κύρια μέθοδος ανάκτησης καιρού.
     *
     * Κάνει external REST call στο OpenWeatherMap forecast API:
     * https://api.openweathermap.org/data/2.5/forecast
     *
     * @param dateTime  Ημερομηνία & ώρα ραντεβού
     * @param location Τοποθεσία (π.χ. "Athens,GR")
     * @return WeatherResponse ή null αν αποτύχει
     */
    @Override
    public WeatherResponse getWeatherFor(final LocalDateTime dateTime,
                                         final String location) {

        // === Έλεγχοι εγκυρότητας παραμέτρων ===
        if (dateTime == null) throw new NullPointerException();
        if (location == null) throw new NullPointerException();
        if (location.isBlank()) throw new IllegalArgumentException();

        // Ανάκτηση API key από configuration
        final String apiKey = this.weatherProperties.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing weather.apiKey (OpenWeather API key)");
        }

        // === Δημιουργία URL για το OpenWeather forecast endpoint ===
        final String url = UriComponentsBuilder
                .fromHttpUrl(this.weatherProperties.getBaseUrl()) // π.χ. https://api.openweathermap.org/data/2.5
                .path("/forecast")
                .queryParam("q", location)      // Τοποθεσία
                .queryParam("appid", apiKey)    // API key
                .queryParam("units", "metric")  // Θερμοκρασία σε °C
                .toUriString();

        try {
            // === HTTP GET request προς OpenWeather ===
            final ResponseEntity<OpenWeatherForecastResponse> response =
                    this.restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            OpenWeatherForecastResponse.class
                    );

            // Έλεγχος αν η απάντηση είναι άδεια ή μη επιτυχής
            if (!response.getStatusCode().is2xxSuccessful()
                    || response.getBody() == null
                    || response.getBody().getList() == null
                    || response.getBody().getList().isEmpty()) {

                LOGGER.warn("OpenWeather returned empty response for location={}", location);
                return null;
            }

            // Μετατροπή της ώρας ραντεβού σε Instant (με timezone Ελλάδας)
            final ZoneId zone = ZoneId.of("Europe/Athens");
            final Instant target = dateTime.atZone(zone).toInstant();

            // Επιλογή forecast entry με τη μικρότερη χρονική απόσταση
            final OpenWeatherForecastResponse.Item nearest =
                    response.getBody().getList().stream()
                            .min(Comparator.comparingLong(
                                    it -> Math.abs(it.getDt() - target.getEpochSecond())
                            ))
                            .orElse(null);

            // Αν δεν βρεθεί κατάλληλο forecast
            if (nearest == null || nearest.getWeather() == null || nearest.getWeather().isEmpty()) {
                LOGGER.warn("OpenWeather cannot match nearest forecast item for location={}", location);
                return null;
            }

            // Ανάγνωση βασικών δεδομένων καιρού
            final String main = nearest.getWeather().get(0).getMain();           // Rain / Clear / Clouds
            final String description = nearest.getWeather().get(0).getDescription();
            final double temp = (nearest.getMain() != null)
                    ? nearest.getMain().getTemp()
                    : Double.NaN;

            // Δημιουργία WeatherResponse για το υπόλοιπο σύστημα
            WeatherResponse wr = new WeatherResponse();
            wr.setLocation(location);
            wr.setDateTime(dateTime);
            wr.setTemperatureC(Double.isNaN(temp) ? null : temp);

            // Δημιουργία φιλικού summary για το UI
            String summary = (main != null ? main : "Weather");
            if (description != null && !description.isBlank()) {
                summary += " (" + description + ")";
            }
            if (!Double.isNaN(temp)) {
                summary += " - " + temp + "°C";
            }

            wr.setSummary(summary);

            LOGGER.info("OpenWeather matched: location={}, summary={}", location, summary);
            return wr;

        } catch (Exception e) {
            // Καταγραφή σφάλματος (π.χ. 401, network error κλπ)
            LOGGER.error("OpenWeather call failed for location={}: {}", location, e.getMessage(), e);
            return null;
        }
    }

    /**
     * POST endpoint.
     *
     * Το OpenWeather δεν υποστηρίζει τέτοια λειτουργία,
     * η μέθοδος υπάρχει μόνο για πληρότητα της εργασίας.
     */
    @Override
    public String postSomethingToWeatherApi(final String message) {
        if (message == null) throw new NullPointerException();
        if (message.isBlank()) throw new IllegalArgumentException();
        return "NOT_SUPPORTED_BY_OPENWEATHER";
    }

    /**
     * Secured GET version.
     *
     * Το OpenWeather δεν χρησιμοποιεί bearer tokens,
     * οπότε καλούμε απλώς την κανονική μέθοδο.
     */
    @Override
    public WeatherResponse getWeatherForSecured(final LocalDateTime dateTime,
                                                final String location) {
        return getWeatherFor(dateTime, location);
    }
}
