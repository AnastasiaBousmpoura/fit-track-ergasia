package gr.hua.dit.fittrack.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OpenWeatherForecastResponse {

    private List<Item> list;

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    public static class Item {
        private long dt; // unix seconds

        @JsonProperty("dt_txt")
        private String dtTxt;

        private Main main;
        private List<Weather> weather;

        public long getDt() { return dt; }
        public void setDt(long dt) { this.dt = dt; }

        public String getDtTxt() { return dtTxt; }
        public void setDtTxt(String dtTxt) { this.dtTxt = dtTxt; }

        public Main getMain() { return main; }
        public void setMain(Main main) { this.main = main; }

        public List<Weather> getWeather() { return weather; }
        public void setWeather(List<Weather> weather) { this.weather = weather; }
    }

    public static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }

        public double getFeelsLike() { return feelsLike; }
        public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    }

    public static class Weather {
        private String main;
        private String description;

        public String getMain() { return main; }
        public void setMain(String main) { this.main = main; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
