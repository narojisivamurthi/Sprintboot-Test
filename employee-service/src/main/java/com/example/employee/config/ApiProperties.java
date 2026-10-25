package com.example.employee.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Enterprise API Configuration Properties.
 * Binds external downstream service URLs and endpoint paths from environment variables
 * or Kubernetes ConfigMaps (e.g. configurationmap.yaml).
 */
@Configuration
@ConfigurationProperties(prefix = "external.services.notification")
@Data
public class ApiProperties {

    /**
     * Base URL of the Notification Service.
     * In local dev: http://localhost:8083
     * In Kubernetes/Docker: http://notification-service:8083 or cluster DNS
     */
    private String baseUrl = "http://localhost:8083";

    /**
     * Path to dispatch standard email
     */
    private String sendEmail = "/api/notifications/send";

    /**
     * Path to dispatch employee onboarding welcome email
     */
    private String sendWelcomeEmail = "/api/notifications/welcome";

    /**
     * Path for downstream health check
     */
    private String health = "/api/notifications/health";
}
