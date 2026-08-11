package com.example.employee.service;

import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.dto.EmployeeResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.entity.Department;
import com.example.employee.entity.Designation;
import com.example.employee.entity.Employee;
import com.example.employee.entity.EmployeeStatus;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.CityRepository;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.DesignationRepository;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DesignationRepository designationRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Department department;
    private Designation designation;
    private City city;
    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        department = new Department("Engineering", "ENG", "Floor 3");
        department.setId("1");

        designation = new Designation("Software Engineer", "SE");
        designation.setId("1");

        city = new City("Bangalore", "Karnataka", "India");
        city.setId("1");

        employee = new Employee();
        employee.setId("1");
        employee.setName("Shiva Naroji");
        employee.setEmail("shiva@example.com");
        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setCity(city);
        employee.setSalary(60000.0);
        employee.setJoiningDate(LocalDate.now());
        employee.setStatus(EmployeeStatus.ACTIVE);

        requestDTO = EmployeeRequestDTO.builder()
                .name("Shiva Naroji")
                .email("shiva@example.com")
                .departmentId("1")
                .designationId("1")
                .cityId("1")
                .salary(60000.0)
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        responseDTO = EmployeeResponseDTO.builder()
                .id("1")
                .name("Shiva Naroji")
                .email("shiva@example.com")
                .salary(60000.0)
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("getAllEmployees should return list of employee response DTOs")
    void getAllEmployees_ShouldReturnList() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Shiva Naroji");
    }

    @Test
    @DisplayName("getEmployeeById should return employee when ID exists")
    void getEmployeeById_Success_ShouldReturnEmployee() {
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.getEmployeeById("1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    @DisplayName("getEmployeeById should throw ResourceNotFoundException when ID does not exist")
    void getEmployeeById_NotFound_ShouldThrowException() {
        when(employeeRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById("99"));
    }

    @Test
    @DisplayName("createEmployee should save employee successfully when valid")
    void createEmployee_Success_ShouldSaveAndReturnResponse() {
        when(employeeRepository.existsByEmail("shiva@example.com")).thenReturn(false);
        when(departmentRepository.findById("1")).thenReturn(Optional.of(department));
        when(designationRepository.findById("1")).thenReturn(Optional.of(designation));
        when(cityRepository.findById("1")).thenReturn(Optional.of(city));
        when(employeeMapper.toEntity(any(), any(), any(), any())).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.createEmployee(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Shiva Naroji");
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("createEmployee should throw DuplicateResourceException when email already exists")
    void createEmployee_DuplicateEmail_ShouldThrowException() {
        when(employeeRepository.existsByEmail("shiva@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(requestDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createEmployee should throw IllegalArgumentException when salary is zero or negative")
    void createEmployee_ZeroSalary_ShouldThrowException() {
        requestDTO.setSalary(0.0);
        when(employeeRepository.existsByEmail("shiva@example.com")).thenReturn(false);
        when(departmentRepository.findById("1")).thenReturn(Optional.of(department));
        when(designationRepository.findById("1")).thenReturn(Optional.of(designation));
        when(cityRepository.findById("1")).thenReturn(Optional.of(city));

        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(requestDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteEmployee should delete existing employee")
    void deleteEmployee_Success_ShouldDelete() {
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeService.deleteEmployee("1");

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    @DisplayName("deleteEmployee should throw ResourceNotFoundException when employee does not exist")
    void deleteEmployee_NotFound_ShouldThrowException() {
        when(employeeRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee("99"));
        verify(employeeRepository, never()).delete(any());
    }
}

