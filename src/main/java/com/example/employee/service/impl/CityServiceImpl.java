package com.example.employee.service.impl;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;
import com.example.employee.entity.City;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.CityMapper;
import com.example.employee.repository.CityRepository;
import com.example.employee.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    @Cacheable(value = "cities", key = "'all'")
    public List<CityResponseDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "cities", key = "#id")
    public CityResponseDTO getCityById(String id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));
        return cityMapper.toResponseDTO(city);
    }

    @Override
    @CacheEvict(value = "cities", allEntries = true)
    public CityResponseDTO createCity(CityRequestDTO dto) {
        if (cityRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("City already exists with name: " + dto.getName());
        }

        City city = cityMapper.toEntity(dto);
        City saved = cityRepository.save(city);
        return cityMapper.toResponseDTO(saved);
    }

    @Override
    @CacheEvict(value = "cities", allEntries = true)
    public CityResponseDTO updateCity(String id, CityRequestDTO dto) {
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

    @Override
    @CacheEvict(value = "cities", allEntries = true)
    public void deleteCity(String id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));
        cityRepository.delete(city);
    }
}
