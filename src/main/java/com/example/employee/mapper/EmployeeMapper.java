package com.example.employee.mapper;

import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.dto.EmployeeResponseDTO;
import com.example.employee.dto.EmployeeSummaryResponseDTO;
import com.example.employee.entity.Department;
import com.example.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final DepartmentMapper departmentMapper;

    public EmployeeMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public Employee toEntity(EmployeeRequestDTO dto, Department department) {
        if (dto == null) {
            return null;
        }
        return new Employee(
                dto.getName(),
                dto.getEmail(),
                department,
                dto.getDesignation(),
                dto.getSalary(),
                dto.getJoiningDate(),
                dto.getCity(),
                dto.getStatus()
        );
    }

    public EmployeeResponseDTO toResponseDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        return EmployeeResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .department(departmentMapper.toResponseDTO(entity.getDepartment()))
                .designation(entity.getDesignation())
                .salary(entity.getSalary())
                .joiningDate(entity.getJoiningDate())
                .city(entity.getCity())
                .status(entity.getStatus())
                .build();
    }

    public EmployeeSummaryResponseDTO toSummaryResponseDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        return EmployeeSummaryResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .designation(entity.getDesignation())
                .salary(entity.getSalary())
                .joiningDate(entity.getJoiningDate())
                .city(entity.getCity())
                .status(entity.getStatus())
                .build();
    }
}
