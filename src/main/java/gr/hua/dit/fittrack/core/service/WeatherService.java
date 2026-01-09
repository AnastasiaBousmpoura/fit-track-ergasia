package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.WeatherResponse;

import java.time.LocalDateTime;

public interface WeatherService {

    WeatherResponse getWeatherFor(final LocalDateTime dateTime, final String location);

    String postSomethingToWeatherApi(final String message);

    WeatherResponse getWeatherForSecured(final LocalDateTime dateTime, final String location);
}
