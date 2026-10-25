package com.example.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response confirming email dispatch status")
public class EmailResponse {

    @Schema(description = "Unique tracking ID for this email dispatch")
    private String trackingId;

    @Schema(description = "Target recipient")
    private String recipient;

    @Schema(description = "Subject line")
    private String subject;

    @Schema(description = "Status of the dispatch (e.g., SENT, SIMULATED, FAILED)")
    private String status;

    @Schema(description = "Timestamp when email was processed")
    private LocalDateTime sentAt;

    @Schema(description = "Descriptive confirmation message")
    private String message;
}
