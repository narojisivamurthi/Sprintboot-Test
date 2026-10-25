package com.example.employee.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * Pure 100% Minimalist Java Record for City Response DTO.
 */
@Builder
public record CityResponseDTO(
        String id,
        String name,
        String state,
        String country
) implements Serializable {}
