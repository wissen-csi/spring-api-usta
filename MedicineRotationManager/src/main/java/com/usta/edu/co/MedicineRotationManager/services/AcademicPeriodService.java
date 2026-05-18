package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.repositories.AcademicPeriodRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AcademicPeriodService {

    private AcademicPeriodRepository academicPeriodRepository;
    private ObjectMapper objectMapper;

    public AcademicPeriodService(
            AcademicPeriodRepository academicPeriodRepository,
            ObjectMapper objectMapper) {

        this.academicPeriodRepository = academicPeriodRepository;
        this.objectMapper = objectMapper;
    }

    public Page<AcademicPeriod> findAll(Pageable pageable) {
        return academicPeriodRepository.findAll(pageable);
    }

    public AcademicPeriod findById(String id) {
        return academicPeriodRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Academic period not found"));
    }

    @Transactional
    public void save(AcademicPeriodCreateDTO dto) {

        AcademicPeriod academicPeriod = AcademicPeriod.builder()
                .id(UUIDGenerator.generateNewId())
                .name(dto.name())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();

        academicPeriodRepository.save(academicPeriod);
    }

    @Transactional
    public void update(String id, AcademicPeriodUpdateDTO dto) {

        AcademicPeriod academicPeriod = academicPeriodRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Academic period not found"));

        academicPeriod.setName(dto.name());
        academicPeriod.setStartDate(dto.startDate());
        academicPeriod.setEndDate(dto.endDate());

        academicPeriodRepository.save(academicPeriod);
    }

    @Transactional
    public void patch(String id, JsonNode node) {

        AcademicPeriod academicPeriod = academicPeriodRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Academic period not found"));

        try {
            objectMapper.readerForUpdating(academicPeriod).readValue(node);

        } catch (Exception e) {
            throw new RuntimeException();
        }

        academicPeriodRepository.save(academicPeriod);
    }

    @Transactional
    public void delete(String id) {

        AcademicPeriod academicPeriod = academicPeriodRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Academic period not found"));

        academicPeriodRepository.delete(academicPeriod);
    }
}
