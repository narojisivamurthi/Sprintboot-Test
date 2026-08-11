package com.example.employee.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class JwtProperties {

    private String jwtSecret;
    private long jwtExpirationMilliseconds;
}
