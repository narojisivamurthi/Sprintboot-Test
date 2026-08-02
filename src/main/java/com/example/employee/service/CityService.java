package com.example.employee.service;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.CityMapper;
import com.example.employee.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Transactional(readOnly = true)
    public List<CityResponseDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CityResponseDTO getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));
        return cityMapper.toResponseDTO(city);
    }

    @Transactional
    public CityResponseDTO createCity(CityRequestDTO dto) {
        if (cityRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("City already exists with name: " + dto.getName());
        }

        City city = cityMapper.toEntity(dto);
        City saved = cityRepository.save(city);
        return cityMapper.toResponseDTO(saved);
    }

    @Transactional
    public CityResponseDTO updateCity(Long id, CityRequestDTO dto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));

        if (!city.getName().equalsIgnoreCase(dto.getName()) && cityRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("City already exists with name: " + dto.getName());
        }

        city.setName(dto.getName());
        if (dto.getState() != null) city.setState(dto.getState());
        if (dto.getCountry() != null) city.setCountry(dto.getCountry());
        City updated = cityRepository.save(city);
        return cityMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));
        cityRepository.delete(city);
    }
}
