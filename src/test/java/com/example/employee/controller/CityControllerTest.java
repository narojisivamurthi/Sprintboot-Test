package com.example.employee.controller;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;
import com.example.employee.service.CityService;
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
class CityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private ObjectMapper objectMapper;
    private CityResponseDTO cityResponseDTO;
    private CityRequestDTO cityRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
        objectMapper = new ObjectMapper();

        cityResponseDTO = CityResponseDTO.builder()
                .id("1")
                .name("Bangalore")
                .state("Karnataka")
                .country("India")
                .build();

        cityRequestDTO = CityRequestDTO.builder()
                .name("Bangalore")
                .state("Karnataka")
                .country("India")
                .build();
    }

    @Test
    @DisplayName("GET /cities should return list of cities with HTTP 200 OK")
    void getAllCities_ShouldReturnCitiesList() throws Exception {
        when(cityService.getAllCities()).thenReturn(List.of(cityResponseDTO));

        mockMvc.perform(get("/cities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Bangalore")))
                .andExpect(jsonPath("$[0].state", is("Karnataka")));

        verify(cityService, times(1)).getAllCities();
    }

    @Test
    @DisplayName("GET /cities/{id} should return city details with HTTP 200 OK")
    void getCityById_ShouldReturnCity() throws Exception {
        when(cityService.getCityById("1")).thenReturn(cityResponseDTO);

        mockMvc.perform(get("/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Bangalore")));

        verify(cityService, times(1)).getCityById("1");
    }

    @Test
    @DisplayName("POST /cities should create city and return HTTP 201 Created")
    void createCity_ShouldReturnCreatedCity() throws Exception {
        when(cityService.createCity(any(CityRequestDTO.class))).thenReturn(cityResponseDTO);

        mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Bangalore")));

        verify(cityService, times(1)).createCity(any(CityRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /cities/{id} should update city and return HTTP 200 OK")
    void updateCity_ShouldReturnUpdatedCity() throws Exception {
        when(cityService.updateCity(eq("1"), any(CityRequestDTO.class))).thenReturn(cityResponseDTO);

        mockMvc.perform(put("/cities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bangalore")));

        verify(cityService, times(1)).updateCity(eq("1"), any(CityRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /cities/{id} should delete city and return HTTP 200 OK")
    void deleteCity_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(cityService).deleteCity("1");

        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("City deleted successfully with ID: 1"));

        verify(cityService, times(1)).deleteCity("1");
    }
}

