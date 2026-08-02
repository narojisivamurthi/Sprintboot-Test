package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSummaryResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String designation;
    private double salary;
    private LocalDate joiningDate;
    private String city;
    private String status;
}
