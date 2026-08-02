package com.example.employee.mapper;

import com.example.employee.dto.CityRequestDTO;
import com.example.employee.dto.CityResponseDTO;
import com.example.employee.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toEntity(CityRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        String state = dto.getState() != null && !dto.getState().isBlank() ? dto.getState() : "N/A";
        String country = dto.getCountry() != null && !dto.getCountry().isBlank() ? dto.getCountry() : "USA";
        return new City(dto.getName(), state, country);
    }

    public CityResponseDTO toResponseDTO(City entity) {
        if (entity == null) {
            return null;
        }
        return CityResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .state(entity.getState())
                .country(entity.getCountry())
                .build();
    }
}
