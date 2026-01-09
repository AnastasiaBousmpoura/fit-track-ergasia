package gr.hua.dit.fittrack.core.model;

import java.time.LocalDateTime;

public class WeatherResponse {
    private String location;
    private LocalDateTime dateTime;
    private Double temperatureC;
    private String summary;

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
