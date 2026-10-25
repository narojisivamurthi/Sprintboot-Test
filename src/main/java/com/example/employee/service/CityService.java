package com.example.employee.service;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;

import java.util.List;

public interface CityService {

    List<CityResponseDTO> getAllCities();

    CityResponseDTO getCityById(String id);

    CityResponseDTO createCity(CityRequestDTO dto);

    CityResponseDTO updateCity(String id, CityRequestDTO dto);

    void deleteCity(String id);
}
