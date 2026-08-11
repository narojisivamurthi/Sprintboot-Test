package com.example.employee.controller;

import com.example.employee.dto.DesignationRequestDTO;
import com.example.employee.dto.DesignationResponseDTO;
import com.example.employee.service.DesignationService;
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
class DesignationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DesignationService designationService;

    @InjectMocks
    private DesignationController designationController;

    private ObjectMapper objectMapper;
    private DesignationResponseDTO responseDTO;
    private DesignationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(designationController).build();
        objectMapper = new ObjectMapper();

        responseDTO = DesignationResponseDTO.builder()
                .id("1")
                .title("Senior Software Engineer")
                .code("SSE")
                .build();

        requestDTO = DesignationRequestDTO.builder()
                .title("Senior Software Engineer")
                .code("SSE")
                .build();
    }

    @Test
    @DisplayName("GET /designations should return designation list with HTTP 200 OK")
    void getAllDesignations_ShouldReturnDesignations() throws Exception {
        when(designationService.getAllDesignations()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/designations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Senior Software Engineer")));

        verify(designationService, times(1)).getAllDesignations();
    }

    @Test
    @DisplayName("GET /designations/{id} should return designation details with HTTP 200 OK")
    void getDesignationById_ShouldReturnDesignation() throws Exception {
        when(designationService.getDesignationById("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/designations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("Senior Software Engineer")));

        verify(designationService, times(1)).getDesignationById("1");
    }

    @Test
    @DisplayName("POST /designations should create designation and return HTTP 201 Created")
    void createDesignation_ShouldReturnCreatedDesignation() throws Exception {
        when(designationService.createDesignation(any(DesignationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/designations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.code", is("SSE")));

        verify(designationService, times(1)).createDesignation(any(DesignationRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /designations/{id} should update designation and return HTTP 200 OK")
    void updateDesignation_ShouldReturnUpdatedDesignation() throws Exception {
        when(designationService.updateDesignation(eq("1"), any(DesignationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/designations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Senior Software Engineer")));

        verify(designationService, times(1)).updateDesignation(eq("1"), any(DesignationRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /designations/{id} should delete designation and return HTTP 200 OK")
    void deleteDesignation_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(designationService).deleteDesignation("1");

        mockMvc.perform(delete("/designations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Designation deleted successfully with ID: 1"));

        verify(designationService, times(1)).deleteDesignation("1");
    }
}

