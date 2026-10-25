package com.example.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for sending an email")
public class EmailRequest {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Must be a valid email address")
    @Schema(description = "Recipient email address", example = "john.doe@company.com")
    private String to;

    @Email(message = "CC must be a valid email address")
    @Schema(description = "CC recipient email address", example = "manager@company.com")
    private String cc;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Email subject line", example = "Welcome to the Team!")
    private String subject;

    @NotBlank(message = "Body is required")
    @Schema(description = "Email body text or HTML markup", example = "Hello John, welcome to our company portal.")
    private String body;

    @Schema(description = "Flag indicating if the body is HTML formatted", defaultValue = "false")
    private boolean isHtml;
}
