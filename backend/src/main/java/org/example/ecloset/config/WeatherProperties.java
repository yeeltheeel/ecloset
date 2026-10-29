package org.example.ecloset.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.weather")
public class WeatherProperties {
    private String baseUrl = "https://wttr.in/";
    private String userAgent = "Wget/1.21";
    private int connectTimeoutSec = 15;
    private int readTimeoutSec = 30;
    private String defaultFormat = "j2";
}