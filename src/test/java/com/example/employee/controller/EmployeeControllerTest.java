package com.example.employee.controller;

import com.example.employee.dto.EmployeeRequestDTO;
import com.example.employee.dto.EmployeeResponseDTO;
import com.example.employee.entity.EmployeeStatus;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;
    private EmployeeResponseDTO responseDTO;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseDTO = EmployeeResponseDTO.builder()
                .id("1")
                .name("Shiva Naroji")
                .email("shiva@example.com")
                .salary(60000.0)
                .status(EmployeeStatus.ACTIVE)
                .build();

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
    }

    @Test
    @DisplayName("GET /employees should return paginated employees")
    void getAllEmployees_ShouldReturnPaginatedList() throws Exception {
        when(employeeService.getEmployeesPaginated(0, 10, "id", "asc"))
                .thenReturn(new PageImpl<>(List.of(responseDTO), org.springframework.data.domain.PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Shiva Naroji")));

        verify(employeeService, times(1)).getEmployeesPaginated(0, 10, "id", "asc");
    }

    @Test
    @DisplayName("GET /employees/all should return unpaginated employee list")
    void getAllEmployeesUnpaginated_ShouldReturnList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Shiva Naroji")));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /employees/{id} should return employee details with HTTP 200 OK")
    void getEmployeeById_ShouldReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Shiva Naroji")));

        verify(employeeService, times(1)).getEmployeeById("1");
    }

    @Test
    @DisplayName("POST /employees should create employee and return HTTP 201 Created")
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Shiva Naroji")));

        verify(employeeService, times(1)).createEmployee(any(EmployeeRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /employees/{id} should update employee and return HTTP 200 OK")
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        when(employeeService.updateEmployee(eq("1"), any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Shiva Naroji")));

        verify(employeeService, times(1)).updateEmployee(eq("1"), any(EmployeeRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /employees/{id} should delete employee and return HTTP 200 OK")
    void deleteEmployee_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(employeeService).deleteEmployee("1");

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully with ID: 1"));

        verify(employeeService, times(1)).deleteEmployee("1");
    }
}

