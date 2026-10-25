package com.example.notification.controller;

import com.example.common.dto.ApiResponse;
import com.example.notification.dto.EmailRequest;
import com.example.notification.dto.EmailResponse;
import com.example.notification.dto.WelcomeEmailRequest;
import com.example.notification.model.NotificationLog;
import com.example.notification.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Endpoints for sending emails and managing notification logs")
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send")
    @Operation(summary = "Send an email", description = "Dispatches a plain text or HTML email to the specified recipient")
    public ResponseEntity<ApiResponse<EmailResponse>> sendEmail(@Valid @RequestBody EmailRequest request) {
        EmailResponse response = emailService.sendEmail(request);
        HttpStatus status = "FAILED".equals(response.getStatus()) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(status.value(), response.getMessage(), response));
    }

    @PostMapping("/welcome")
    @Operation(summary = "Send onboarding welcome email", description = "Dispatches a branded HTML welcome email with employee portal credentials")
    public ResponseEntity<ApiResponse<EmailResponse>> sendWelcomeEmail(@Valid @RequestBody WelcomeEmailRequest request) {
        EmailResponse response = emailService.sendWelcomeEmail(request);
        HttpStatus status = "FAILED".equals(response.getStatus()) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(status.value(), response.getMessage(), response));
    }

    @GetMapping("/history")
    @Operation(summary = "Get notification history", description = "Retrieves recent sent and simulated email activity logs")
    public ResponseEntity<ApiResponse<List<NotificationLog>>> getHistory() {
        List<NotificationLog> history = emailService.getNotificationHistory();
        return ResponseEntity.ok(ApiResponse.success("Retrieved notification history", history));
    }

    @GetMapping("/health")
    @Operation(summary = "Notification Service Health Check", description = "Returns service health and status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Notification service is operational", Map.of(
                "service", "notification-service",
                "status", "UP",
                "port", 8083,
                "timestamp", LocalDateTime.now()
        )));
    }
}
