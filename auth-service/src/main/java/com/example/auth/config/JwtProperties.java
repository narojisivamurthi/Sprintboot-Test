package com.example.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class JwtProperties {

    private String jwtSecret = "9a2f8c4e7b1d3a5f8e0c2b4a6f8d1e3c5b7a9f0e2d4c6b8a1f3e5d7c9b0a2f4e";
    private long jwtExpirationMilliseconds = 86400000;
}
