package com.example.employee.service;

import com.example.employee.dto.DepartmentRequestDTO;
import com.example.employee.dto.DepartmentResponseDTO;
import com.example.employee.dto.DepartmentWithEmployeesResponseDTO;
import com.example.employee.entity.Department;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.DepartmentMapper;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentRequestDTO requestDTO;
    private DepartmentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        department = new Department("Engineering", "ENG", "Floor 3");
        department.setId(1L);

        requestDTO = DepartmentRequestDTO.builder()
                .name("Engineering")
                .code("ENG")
                .location("Floor 3")
                .build();

        responseDTO = DepartmentResponseDTO.builder()
                .id(1L)
                .name("Engineering")
                .code("ENG")
                .location("Floor 3")
                .build();
    }

    @Test
    @DisplayName("getAllDepartments should return list of DepartmentResponseDTOs")
    void getAllDepartments_ShouldReturnList() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(departmentMapper.toResponseDTO(department)).thenReturn(responseDTO);

        List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("ENG");
    }

    @Test
    @DisplayName("getDepartmentById should return DepartmentResponseDTO when exists")
    void getDepartmentById_Success_ShouldReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponseDTO(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = departmentService.getDepartmentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getDepartmentById should throw ResourceNotFoundException when not found")
    void getDepartmentById_NotFound_ShouldThrowException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(99L));
    }

    @Test
    @DisplayName("createDepartment should save and return DepartmentResponseDTO")
    void createDepartment_Success_ShouldSaveDepartment() {
        when(departmentRepository.existsByCode("ENG")).thenReturn(false);
        when(departmentRepository.existsByName("Engineering")).thenReturn(false);
        when(departmentMapper.toEntity(requestDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toResponseDTO(department)).thenReturn(responseDTO);

        DepartmentResponseDTO result = departmentService.createDepartment(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("ENG");
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    @DisplayName("createDepartment should throw DuplicateResourceException when code exists")
    void createDepartment_DuplicateCode_ShouldThrowException() {
        when(departmentRepository.existsByCode("ENG")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> departmentService.createDepartment(requestDTO));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("createDepartment should throw DuplicateResourceException when name exists")
    void createDepartment_DuplicateName_ShouldThrowException() {
        when(departmentRepository.existsByCode("ENG")).thenReturn(false);
        when(departmentRepository.existsByName("Engineering")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> departmentService.createDepartment(requestDTO));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteDepartment should delete department when found")
    void deleteDepartment_Success_ShouldDelete() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).delete(department);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).delete(department);
    }
}
