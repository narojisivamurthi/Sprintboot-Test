package com.example.employee.service;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.CityMapper;
import com.example.employee.repository.CityRepository;
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
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    private City city;
    private CityRequestDTO cityRequestDTO;
    private CityResponseDTO cityResponseDTO;

    @BeforeEach
    void setUp() {
        city = new City("Bangalore", "Karnataka", "India");
        city.setId(1L);

        cityRequestDTO = CityRequestDTO.builder()
                .name("Bangalore")
                .state("Karnataka")
                .country("India")
                .build();

        cityResponseDTO = CityResponseDTO.builder()
                .id(1L)
                .name("Bangalore")
                .state("Karnataka")
                .country("India")
                .build();
    }

    @Test
    @DisplayName("getAllCities should return list of city response DTOs")
    void getAllCities_ShouldReturnListOfCityResponseDTO() {
        when(cityRepository.findAll()).thenReturn(List.of(city));
        when(cityMapper.toResponseDTO(city)).thenReturn(cityResponseDTO);

        List<CityResponseDTO> result = cityService.getAllCities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Bangalore");
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getCityById should return CityResponseDTO when city exists")
    void getCityById_Success_ShouldReturnCityResponseDTO() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityMapper.toResponseDTO(city)).thenReturn(cityResponseDTO);

        CityResponseDTO result = cityService.getCityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Bangalore");
    }

    @Test
    @DisplayName("getCityById should throw ResourceNotFoundException when city does not exist")
    void getCityById_NotFound_ShouldThrowException() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCityById(99L));
    }

    @Test
    @DisplayName("createCity should save and return CityResponseDTO")
    void createCity_Success_ShouldSaveAndReturnCity() {
        when(cityRepository.existsByName("Bangalore")).thenReturn(false);
        when(cityMapper.toEntity(cityRequestDTO)).thenReturn(city);
        when(cityRepository.save(city)).thenReturn(city);
        when(cityMapper.toResponseDTO(city)).thenReturn(cityResponseDTO);

        CityResponseDTO result = cityService.createCity(cityRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Bangalore");
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    @DisplayName("createCity should throw DuplicateResourceException when city name exists")
    void createCity_DuplicateName_ShouldThrowException() {
        when(cityRepository.existsByName("Bangalore")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> cityService.createCity(cityRequestDTO));
        verify(cityRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateCity should update and return CityResponseDTO when valid")
    void updateCity_Success_ShouldUpdateAndReturnCity() {
        CityRequestDTO updateDTO = CityRequestDTO.builder()
                .name("Bangalore Updated")
                .state("Karnataka")
                .country("India")
                .build();

        CityResponseDTO updatedResponse = CityResponseDTO.builder()
                .id(1L)
                .name("Bangalore Updated")
                .state("Karnataka")
                .country("India")
                .build();

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityRepository.existsByName("Bangalore Updated")).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        when(cityMapper.toResponseDTO(any(City.class))).thenReturn(updatedResponse);

        CityResponseDTO result = cityService.updateCity(1L, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Bangalore Updated");
    }

    @Test
    @DisplayName("deleteCity should remove city when it exists")
    void deleteCity_Success_ShouldDeleteCity() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        doNothing().when(cityRepository).delete(city);

        cityService.deleteCity(1L);

        verify(cityRepository, times(1)).delete(city);
    }

    @Test
    @DisplayName("deleteCity should throw ResourceNotFoundException when city does not exist")
    void deleteCity_NotFound_ShouldThrowException() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(99L));
        verify(cityRepository, never()).delete(any());
    }
}
