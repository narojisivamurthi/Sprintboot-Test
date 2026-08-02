package com.example.employee.service;

import com.example.employee.dto.DesignationRequestDTO;
import com.example.employee.dto.DesignationResponseDTO;
import com.example.employee.entity.Designation;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.DesignationMapper;
import com.example.employee.repository.DesignationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;

    public DesignationService(DesignationRepository designationRepository, DesignationMapper designationMapper) {
        this.designationRepository = designationRepository;
        this.designationMapper = designationMapper;
    }

    @Transactional(readOnly = true)
    public List<DesignationResponseDTO> getAllDesignations() {
        return designationRepository.findAll().stream()
                .map(designationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DesignationResponseDTO getDesignationById(Long id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + id));
        return designationMapper.toResponseDTO(designation);
    }

    @Transactional
    public DesignationResponseDTO createDesignation(DesignationRequestDTO dto) {
        if (designationRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateResourceException("Designation already exists with title: " + dto.getTitle());
        }

        String code = dto.getCode() != null && !dto.getCode().isBlank()
                ? dto.getCode().toUpperCase()
                : generateUniqueDesignationCode(dto.getTitle());

        if (designationRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Designation already exists with code: " + code);
        }

        Designation designation = designationMapper.toEntity(dto, code);
        Designation saved = designationRepository.save(designation);
        return designationMapper.toResponseDTO(saved);
    }

    @Transactional
    public DesignationResponseDTO updateDesignation(Long id, DesignationRequestDTO dto) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + id));

        if (!designation.getTitle().equalsIgnoreCase(dto.getTitle()) && designationRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateResourceException("Designation already exists with title: " + dto.getTitle());
        }

        designation.setTitle(dto.getTitle());
        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            designation.setCode(dto.getCode().toUpperCase());
        }
        Designation updated = designationRepository.save(designation);
        return designationMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deleteDesignation(Long id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + id));
        designationRepository.delete(designation);
    }

    private String generateUniqueDesignationCode(String desigTitle) {
        String baseCode = generateCode(desigTitle);
        String code = baseCode;
        int counter = 1;
        while (designationRepository.existsByCode(code)) {
            code = baseCode + "-" + counter;
            counter++;
        }
        return code;
    }

    private String generateCode(String name) {
        if (name == null || name.trim().isEmpty()) return "GEN";
        String[] words = name.split("\\s+");
        if (words.length == 1) {
            return name.substring(0, Math.min(3, name.length())).toUpperCase();
        }
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.equalsIgnoreCase("&")) {
                sb.append(w.charAt(0));
            }
        }
        return sb.toString().toUpperCase();
    }
}
