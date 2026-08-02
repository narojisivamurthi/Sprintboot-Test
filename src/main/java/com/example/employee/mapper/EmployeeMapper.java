package com.example.employee.mapper;

import com.example.employee.dto.CityResponseDTO;
import com.example.employee.dto.DesignationResponseDTO;
import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.dto.EmployeeResponseDTO;
import com.example.employee.dto.EmployeeSummaryResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.entity.Department;
import com.example.employee.entity.Designation;
import com.example.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final DepartmentMapper departmentMapper;

    public EmployeeMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public Employee toEntity(EmployeeRequestDTO dto, Department department, Designation designation, City city) {
        if (dto == null) {
            return null;
        }
        return new Employee(
                dto.getName(),
                dto.getEmail(),
                department,
                designation,
                dto.getSalary(),
                dto.getJoiningDate(),
                city,
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
                .designation(toDesignationDTO(entity.getDesignation()))
                .salary(entity.getSalary())
                .joiningDate(entity.getJoiningDate())
                .city(toCityDTO(entity.getCity()))
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
                .designation(entity.getDesignation() != null ? entity.getDesignation().getTitle() : null)
                .salary(entity.getSalary())
                .joiningDate(entity.getJoiningDate())
                .city(entity.getCity() != null ? entity.getCity().getName() : null)
                .status(entity.getStatus())
                .build();
    }

    public DesignationResponseDTO toDesignationDTO(Designation designation) {
        if (designation == null) {
            return null;
        }
        return DesignationResponseDTO.builder()
                .id(designation.getId())
                .title(designation.getTitle())
                .code(designation.getCode())
                .build();
    }

    public CityResponseDTO toCityDTO(City city) {
        if (city == null) {
            return null;
        }
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }
}
