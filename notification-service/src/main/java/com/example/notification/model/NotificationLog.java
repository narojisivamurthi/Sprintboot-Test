package com.example.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    private String trackingId;
    private String recipient;
    private String cc;
    private String subject;
    private String type; // TEXT, HTML, WELCOME
    private String status; // SENT, SIMULATED, FAILED
    private String errorMessage;
    private LocalDateTime timestamp;
}
