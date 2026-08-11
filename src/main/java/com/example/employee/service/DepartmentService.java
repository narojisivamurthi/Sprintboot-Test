package com.example.employee.service;

import com.example.employee.dto.DepartmentRequestDTO;
import com.example.employee.dto.DepartmentResponseDTO;
import com.example.employee.dto.DepartmentWithEmployeesResponseDTO;
import com.example.employee.dto.EmployeeSummaryResponseDTO;
import com.example.employee.entity.Department;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.DepartmentMapper;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository,
                             DepartmentMapper departmentMapper,
                             EmployeeMapper employeeMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentMapper = departmentMapper;
        this.employeeMapper = employeeMapper;
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DepartmentResponseDTO getDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return departmentMapper.toResponseDTO(department);
    }

    public DepartmentWithEmployeesResponseDTO getDepartmentWithEmployees(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        List<EmployeeSummaryResponseDTO> employeeDTOs = employees.stream()
                .map(employeeMapper::toSummaryResponseDTO)
                .collect(Collectors.toList());

        return departmentMapper.toWithEmployeesDTO(department, employeeDTOs);
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {
        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Department already exists with code: " + dto.getCode());
        }
        if (departmentRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Department already exists with name: " + dto.getName());
        }

        Department department = departmentMapper.toEntity(dto);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toResponseDTO(savedDepartment);
    }

    public DepartmentResponseDTO updateDepartment(String id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setLocation(dto.getLocation());
        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponseDTO(updated);
    }

    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        departmentRepository.delete(department);
    }
}

