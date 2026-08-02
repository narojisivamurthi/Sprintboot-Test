package com.example.employee.service;

import com.example.employee.dto.DepartmentRequestDTO;
import com.example.employee.dto.DepartmentResponseDTO;
import com.example.employee.dto.DepartmentWithEmployeesResponseDTO;
import com.example.employee.dto.EmployeeSummaryResponseDTO;
import com.example.employee.entity.Department;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.DepartmentMapper;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             DepartmentMapper departmentMapper,
                             EmployeeMapper employeeMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.employeeMapper = employeeMapper;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return departmentMapper.toResponseDTO(department);
    }

    @Transactional(readOnly = true)
    public DepartmentWithEmployeesResponseDTO getDepartmentWithEmployees(Long id) {
        // Fetching with JOIN FETCH inside @Transactional ensures lazy loaded employees are initialized safely
        Department department = departmentRepository.findByIdWithEmployees(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        
        List<EmployeeSummaryResponseDTO> employeeDTOs = department.getEmployees().stream()
                .map(employeeMapper::toSummaryResponseDTO)
                .collect(Collectors.toList());

        return departmentMapper.toWithEmployeesDTO(department, employeeDTOs);
    }

    @Transactional
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

    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setLocation(dto.getLocation());
        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        departmentRepository.delete(department);
    }
}
