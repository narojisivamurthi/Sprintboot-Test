package com.example.employee.client;

import com.example.employee.config.ApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Downstream HTTP client for communicating with Notification Service.
 * Leverages ApiProperties to dynamically construct endpoint URLs without hardcoding strings.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationClient {

    private final ApiProperties apiProperties;
    private final RestClient restClient = RestClient.create();

    /**
     * Dispatches an onboarding welcome email asynchronously via the Notification Service.
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String employeeName, String department, String designation) {
        if (toEmail == null || toEmail.isBlank()) {
            log.warn("[NotificationClient] Skipping welcome email: no email address provided");
            return;
        }

        try {
            // Enterprise pattern: dynamic URL composition using ApiProperties
            String targetUrl = apiProperties.getBaseUrl() + apiProperties.getSendWelcomeEmail();
            log.info("[NotificationClient] Calling external Notification Service at: {}", targetUrl);

            Map<String, Object> payload = new HashMap<>();
            payload.put("to", toEmail);
            payload.put("employeeName", employeeName != null ? employeeName : "Team Member");
            payload.put("department", department != null ? department : "General");
            payload.put("designation", designation != null ? designation : "Associate");
            payload.put("temporaryPassword", "Welcome#" + (int) (Math.random() * 9000 + 1000));

            restClient.post()
                    .uri(targetUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            log.info("[NotificationClient] Welcome email successfully triggered for {}", toEmail);

        } catch (Exception e) {
            // Graceful non-blocking degradation if notification service is unreachable
            log.warn("[NotificationClient] Failed to dispatch welcome email to {} (non-blocking fallback): {}",
                    toEmail, e.getMessage());
        }
    }
}
