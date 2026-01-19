package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.service.WeatherService;
import gr.hua.dit.fittrack.core.model.WeatherResponse;
import java.time.LocalDateTime;

// Mock υλοποίηση του WeatherService για δοκιμές
public class MockWeatherService implements WeatherService {

    @Override
    public WeatherResponse getWeatherFor(final LocalDateTime dateTime, final String location) {
        WeatherResponse r = new WeatherResponse();
        r.setLocation(location);
        r.setDateTime(dateTime);
        r.setTemperatureC(21.0);
        r.setSummary("MOCK: Sunny");
        return r;
    }

    @Override
    public String postSomethingToWeatherApi(final String message) {
        return "MOCK: OK (" + message + ")";
    }

    @Override
    public WeatherResponse getWeatherForSecured(final LocalDateTime dateTime, final String location) {
        WeatherResponse r = new WeatherResponse();
        r.setLocation(location);
        r.setDateTime(dateTime);
        r.setTemperatureC(19.0);
        r.setSummary("MOCK: Secured OK");
        return r;
    }
}
