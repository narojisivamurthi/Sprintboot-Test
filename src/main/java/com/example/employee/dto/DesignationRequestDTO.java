package com.example.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignationRequestDTO {

    @NotBlank(message = "Designation title is required")
    @Size(min = 2, max = 100, message = "Designation title must be between 2 and 100 characters")
    private String title;

    private String code;
}
