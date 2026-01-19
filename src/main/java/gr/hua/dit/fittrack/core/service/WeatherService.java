package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.WeatherResponse;
import java.time.LocalDateTime;

public interface WeatherService {
    //GET request για τον καιρό σε συγκεκριμένη ημερομηνία/ώρα και τοποθεσία.
    WeatherResponse getWeatherFor(final LocalDateTime dateTime, final String location);
    //POST request προς την εξωτερική υπηρεσία
    String postSomethingToWeatherApi(final String message);
    // GET request με authentication token
    WeatherResponse getWeatherForSecured(final LocalDateTime dateTime, final String location);
}
