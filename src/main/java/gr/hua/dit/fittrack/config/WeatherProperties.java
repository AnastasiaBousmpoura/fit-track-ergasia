package gr.hua.dit.fittrack.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "weather")
@Component
public class WeatherProperties {

    /**
     * Base URL of the weather API, e.g. https://api.example.com
     */
    private String baseUrl;

    private String apiKey;

    /**
     * Optional client id/secret if your API uses OAuth client_credentials (Routee-like).
     */
    private String appId;
    private String appSecret;


    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }

    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}
