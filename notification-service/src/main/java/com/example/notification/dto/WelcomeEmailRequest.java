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
@Schema(description = "Request payload for sending an onboarding welcome email")
public class WelcomeEmailRequest {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Must be a valid email address")
    @Schema(description = "Employee email address", example = "alice.smith@company.com")
    private String to;

    @NotBlank(message = "Employee name is required")
    @Schema(description = "Employee full name", example = "Alice Smith")
    private String employeeName;

    @Schema(description = "Assigned department", example = "Engineering")
    private String department;

    @Schema(description = "Assigned designation/title", example = "Senior Software Engineer")
    private String designation;

    @Schema(description = "Temporary portal password", example = "Welcome#2026")
    private String temporaryPassword;
}
