package com.example.employee.controller;

import com.example.employee.dto.DepartmentRequestDTO;
import com.example.employee.dto.DepartmentResponseDTO;
import com.example.employee.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private ObjectMapper objectMapper;
    private DepartmentResponseDTO responseDTO;
    private DepartmentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        objectMapper = new ObjectMapper();

        responseDTO = DepartmentResponseDTO.builder()
                .id(1L)
                .name("Engineering")
                .code("ENG")
                .location("Floor 3")
                .build();

        requestDTO = DepartmentRequestDTO.builder()
                .name("Engineering")
                .code("ENG")
                .location("Floor 3")
                .build();
    }

    @Test
    @DisplayName("GET /departments should return department list with HTTP 200 OK")
    void getAllDepartments_ShouldReturnDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/departments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is("ENG")));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    @DisplayName("GET /departments/{id} should return department details with HTTP 200 OK")
    void getDepartmentById_ShouldReturnDepartment() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/departments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Engineering")));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @DisplayName("POST /departments should create department and return HTTP 201 Created")
    void createDepartment_ShouldReturnCreatedDepartment() throws Exception {
        when(departmentService.createDepartment(any(DepartmentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("ENG")));

        verify(departmentService, times(1)).createDepartment(any(DepartmentRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /departments/{id} should update department and return HTTP 200 OK")
    void updateDepartment_ShouldReturnUpdatedDepartment() throws Exception {
        when(departmentService.updateDepartment(eq(1L), any(DepartmentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/departments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Engineering")));

        verify(departmentService, times(1)).updateDepartment(eq(1L), any(DepartmentRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /departments/{id} should delete department and return HTTP 200 OK")
    void deleteDepartment_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Department deleted successfully with ID: 1"));

        verify(departmentService, times(1)).deleteDepartment(1L);
    }
}
