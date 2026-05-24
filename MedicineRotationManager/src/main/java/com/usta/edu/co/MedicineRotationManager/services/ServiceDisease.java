package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.DiseaseResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.Disease;
import com.usta.edu.co.MedicineRotationManager.repositories.DiseaseRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceDisease {
    private final DiseaseRepository repository;

    public ServiceDisease(DiseaseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(DiseaseCreateDTO dto) {
        Disease disease = Disease.builder()
                .id(dto.id() != null ? dto.id() : UUIDGenerator.generateNewId())
                .code(dto.code())
                .name(dto.name())
                .definition(dto.definition())
                .build();
        repository.save(disease);
    }

    public Page<Disease> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Disease findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Disease not found"));
    }

    @Transactional
    public void update(String id, DiseaseCreateDTO dto) {
        Disease disease = findById(id);
        if (dto.code() != null) disease.setCode(dto.code());
        if (dto.name() != null) disease.setName(dto.name());
        if (dto.definition() != null) disease.setDefinition(dto.definition());
        repository.save(disease);
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public DiseaseResponseDTO convertToDTO(Disease disease) {
        return DiseaseResponseDTO.builder()
                .id(disease.getId())
                .code(disease.getCode())
                .name(disease.getName())
                .definition(disease.getDefinition())
                .build();
    }
}
