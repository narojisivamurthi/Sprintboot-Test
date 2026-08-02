package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentWithEmployeesResponseDTO {

    private Long id;
    private String name;
    private String code;
    private String location;
    private List<EmployeeSummaryResponseDTO> employees;
}
