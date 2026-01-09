package gr.hua.dit.fittrack.web.rest;

import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.core.model.WeatherResponse;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST API Resource class for managing Weather information.
 *
 * External API example (GET, POST, secured).
 *
 * @author Dimitris Gkoulis
 */
@RestController
@RequestMapping(value = "/api/v1/weather", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherResource.class);

    private final WeatherService weatherService;

    public WeatherResource(final WeatherService weatherService) {
        if (weatherService == null) throw new NullPointerException();
        this.weatherService = weatherService;
    }

    // --------------------------------------------------
    // GET (External API)
    // --------------------------------------------------

    @GetMapping(value = "")
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final LocalDateTime dateTime,

            @RequestParam
            @NotBlank
            final String location) {

        final WeatherResponse response =
                this.weatherService.getWeatherFor(dateTime, location);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // POST (External API – mock example)
    // --------------------------------------------------

    @PostMapping(value = "/something", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postSomething(
            @RequestBody Map<String, Object> body) {

        final String message = (String) body.get("message");
        final String result = this.weatherService.postSomethingToWeatherApi(message);

        return ResponseEntity.ok(Map.of("result", result));
    }

    // --------------------------------------------------
    // GET (Secured External API – Bearer token)
    // --------------------------------------------------

    @GetMapping(value = "/secured")
    public ResponseEntity<WeatherResponse> getWeatherSecured(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final LocalDateTime dateTime,

            @RequestParam
            @NotBlank
            final String location) {

        final WeatherResponse response =
                this.weatherService.getWeatherForSecured(dateTime, location);

        return ResponseEntity.ok(response);
    }
}

