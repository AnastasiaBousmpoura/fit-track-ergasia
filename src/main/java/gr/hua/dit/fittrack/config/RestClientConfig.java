package gr.hua.dit.fittrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Configuration για HTTP client
@Configuration
public class RestClientConfig {

    // Bean για RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

