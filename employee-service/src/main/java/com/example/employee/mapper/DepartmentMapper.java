package com.example.employee.mapper;

import com.example.employee.dto.DepartmentRequestDTO;
import com.example.employee.dto.DepartmentResponseDTO;
import com.example.employee.dto.DepartmentWithEmployeesResponseDTO;
import com.example.employee.dto.EmployeeSummaryResponseDTO;
import com.example.employee.entity.Department;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Department(dto.getName(), dto.getCode(), dto.getLocation());
    }

    public DepartmentResponseDTO toResponseDTO(Department entity) {
        if (entity == null) {
            return null;
        }
        return DepartmentResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .location(entity.getLocation())
                .build();
    }

    public DepartmentWithEmployeesResponseDTO toWithEmployeesDTO(Department entity, List<EmployeeSummaryResponseDTO> employeeDTOs) {
        if (entity == null) {
            return null;
        }
        return DepartmentWithEmployeesResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .location(entity.getLocation())
                .employees(employeeDTOs != null ? employeeDTOs : Collections.emptyList())
                .build();
    }
}
