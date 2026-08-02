package com.example.employee.mapper;

import com.example.employee.dto.DesignationRequestDTO;
import com.example.employee.dto.DesignationResponseDTO;
import com.example.employee.entity.Designation;
import org.springframework.stereotype.Component;

@Component
public class DesignationMapper {

    public Designation toEntity(DesignationRequestDTO dto, String generatedCode) {
        if (dto == null) {
            return null;
        }
        String code = dto.getCode() != null && !dto.getCode().isBlank()
                ? dto.getCode().toUpperCase()
                : generatedCode;
        return new Designation(dto.getTitle(), code);
    }

    public DesignationResponseDTO toResponseDTO(Designation entity) {
        if (entity == null) {
            return null;
        }
        return DesignationResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .code(entity.getCode())
                .build();
    }
}
