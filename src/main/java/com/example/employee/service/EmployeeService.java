package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.dto.EmployeeResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.entity.Department;
import com.example.employee.entity.Designation;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.CityRepository;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.DesignationRepository;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final CityRepository cityRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           DesignationRepository designationRepository,
                           CityRepository cityRepository,
                           EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.cityRepository = cityRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployeesPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable).map(employeeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return employeeMapper.toResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (dto.getEmail() != null && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + dto.getEmail());
        }
        Department department = resolveDepartment(dto);
        Designation designation = resolveDesignation(dto);
        City city = resolveCity(dto);
        validateSalary(dto, designation);

        Employee employee = employeeMapper.toEntity(dto, department, designation, city);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (dto.getEmail() != null && !existingEmployee.getEmail().equalsIgnoreCase(dto.getEmail())
                && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + dto.getEmail());
        }

        Department department = resolveDepartment(dto);
        Designation designation = resolveDesignation(dto);
        City city = resolveCity(dto);
        validateSalary(dto, designation);

        existingEmployee.setName(dto.getName());
        existingEmployee.setEmail(dto.getEmail());
        existingEmployee.setDepartment(department);
        existingEmployee.setDesignation(designation);
        existingEmployee.setSalary(dto.getSalary());
        existingEmployee.setJoiningDate(dto.getJoiningDate());
        existingEmployee.setCity(city);
        existingEmployee.setStatus(dto.getStatus());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toResponseDTO(updatedEmployee);
    }

    private void validateSalary(EmployeeRequestDTO dto, Designation designation) {
        if (dto.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be greater than zero");
        }

        LocalDate joiningDate = dto.getJoiningDate() != null ? dto.getJoiningDate() : LocalDate.now();
        LocalDate now = LocalDate.now();
        
        int experienceYears = 1;
        if (joiningDate.isBefore(now)) {
            experienceYears = Math.max(1, Period.between(joiningDate, now).getYears());
        }

        double baseYearlyRate = 12000.0;
        String designationTitle = designation != null ? designation.getTitle().toLowerCase() : "";
        if (designationTitle.contains("senior") || designationTitle.contains("lead")) {
            baseYearlyRate = 18000.0;
        } else if (designationTitle.contains("architect") || designationTitle.contains("manager") || designationTitle.contains("director")) {
            baseYearlyRate = 22000.0;
        }

        double minRequiredSalary = experienceYears * baseYearlyRate;

        if (dto.getSalary() < minRequiredSalary) {
            throw new IllegalArgumentException(String.format(
                    "Salary of $%.2f is invalid. Minimum required salary is $%.2f for '%s' with %d year(s) of experience ($%.2f base/year).",
                    dto.getSalary(), minRequiredSalary, designationTitle, experienceYears, baseYearlyRate
            ));
        }
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        employeeRepository.delete(existingEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployeesByDepartmentPaginated(Long departmentId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findByDepartmentId(departmentId, pageable).map(employeeMapper::toResponseDTO);
    }

    private Department resolveDepartment(EmployeeRequestDTO dto) {
        if (dto.getDepartmentId() != null) {
            return departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
        }

        if (dto.getDepartmentCode() != null && !dto.getDepartmentCode().isBlank()) {
            Optional<Department> deptOpt = departmentRepository.findByCode(dto.getDepartmentCode());
            if (deptOpt.isPresent()) {
                return deptOpt.get();
            }
        }

        if (dto.getDepartmentName() != null && !dto.getDepartmentName().isBlank()) {
            Optional<Department> deptOpt = departmentRepository.findByName(dto.getDepartmentName());
            if (deptOpt.isPresent()) {
                return deptOpt.get();
            }
            String code = dto.getDepartmentCode() != null && !dto.getDepartmentCode().isBlank()
                    ? dto.getDepartmentCode()
                    : dto.getDepartmentName().substring(0, Math.min(3, dto.getDepartmentName().length())).toUpperCase();
            return departmentRepository.save(new Department(dto.getDepartmentName(), code, "General Office"));
        }

        throw new ResourceNotFoundException("Department specification required (departmentId, departmentCode, or departmentName)");
    }

    private Designation resolveDesignation(EmployeeRequestDTO dto) {
        if (dto.getDesignationId() != null) {
            return designationRepository.findById(dto.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + dto.getDesignationId()));
        }

        if (dto.getDesignationCode() != null && !dto.getDesignationCode().isBlank()) {
            Optional<Designation> desigOpt = designationRepository.findByCode(dto.getDesignationCode());
            if (desigOpt.isPresent()) {
                return desigOpt.get();
            }
        }

        if (dto.getDesignationTitle() != null && !dto.getDesignationTitle().isBlank()) {
            Optional<Designation> desigOpt = designationRepository.findByTitle(dto.getDesignationTitle());
            if (desigOpt.isPresent()) {
                return desigOpt.get();
            }
            String code = dto.getDesignationCode() != null && !dto.getDesignationCode().isBlank()
                    ? dto.getDesignationCode()
                    : generateUniqueDesignationCode(dto.getDesignationTitle());
            return designationRepository.save(new Designation(dto.getDesignationTitle(), code));
        }

        throw new ResourceNotFoundException("Designation specification required (designationId, designationCode, or designationTitle)");
    }

    private City resolveCity(EmployeeRequestDTO dto) {
        if (dto.getCityId() != null) {
            return cityRepository.findById(dto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + dto.getCityId()));
        }

        if (dto.getCityName() != null && !dto.getCityName().isBlank()) {
            Optional<City> cityOpt = cityRepository.findByName(dto.getCityName());
            if (cityOpt.isPresent()) {
                return cityOpt.get();
            }
            return cityRepository.save(new City(dto.getCityName(), "N/A", "USA"));
        }

        return null;
    }

    private String generateUniqueDesignationCode(String desigTitle) {
        String baseCode = generateCode(desigTitle);
        String code = baseCode;
        int counter = 1;
        while (designationRepository.existsByCode(code)) {
            code = baseCode + "-" + counter;
            counter++;
        }
        return code;
    }

    private String generateCode(String name) {
        if (name == null || name.trim().isEmpty()) return "GEN";
        String[] words = name.split("\\s+");
        if (words.length == 1) {
            return name.substring(0, Math.min(3, name.length())).toUpperCase();
        }
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.equalsIgnoreCase("&")) {
                sb.append(w.charAt(0));
            }
        }
        return sb.toString().toUpperCase();
    }
}
