package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.config.WeatherProperties;
import gr.hua.dit.fittrack.core.service.impl.MockWeatherService;
import gr.hua.dit.fittrack.core.service.impl.WeatherApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherServiceSelector {

    @Bean
    public WeatherApiService weatherApiService(final RestTemplate restTemplate,
                                               final WeatherProperties weatherProperties) {
        return new WeatherApiService(restTemplate, weatherProperties);
    }

    @Bean
    public MockWeatherService mockWeatherService() {
        return new MockWeatherService();
    }

    @Bean
    public WeatherService weatherService(final WeatherProperties weatherProperties,
                                         final WeatherApiService weatherApiService,
                                         final MockWeatherService mockWeatherService) {

        if (StringUtils.hasText(weatherProperties.getBaseUrl())) {
            return weatherApiService;
        } else {
            return mockWeatherService;
        }
    }
}
