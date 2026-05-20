
package com.usta.edu.co.MedicineRotationManager.services;

<<<<<<< HEAD
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> origin/features-crud
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.repositories.AcademicPeriodRepository;
<<<<<<< HEAD
import com.usta.edu.co.MedicineRotationManager.utils.DateValidator;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicPeriodService {

        private final AcademicPeriodRepository academicPeriodRepository;

        @Transactional(readOnly = true)
        public AcademicPeriod findById(String id) {

                return this.academicPeriodRepository.findById(id)

                                .orElseThrow(() ->

                                new EntityNotFoundException(
                                                "Academic period not found with id: " + id));
        }

        @Transactional
        public void save(AcademicPeriodCreateDTO dto) {
                DateValidator.validateDateRange(
                                dto.startDate(),
                                dto.endDate());

                boolean exists = academicPeriodRepository
                                .existsByName(dto.name());

                if (exists) {
                        throw new IllegalArgumentException(
                                        "Academic period already exists");
                }
                AcademicPeriod academicPeriod = AcademicPeriod.builder()

                                .id(UUIDGenerator.generateNewId())
                                .name(dto.name())
                                .startDate(dto.startDate())
                                .endDate(dto.endDate())
                                .isActive(true)

                                .build();

                academicPeriodRepository.save(
                                academicPeriod);
        }

        @Transactional
        public void update(AcademicPeriodUpdateDTO dto, String id) {

                AcademicPeriod academicPeriod = findById(id);
                DateValidator.validateDateRange(

                                dto.startDate(),
                                dto.endDate());

                boolean duplicatedName = academicPeriodRepository
                                .existsByNameAndIdNot(
                                                dto.name(),
                                                id);

                if (duplicatedName) {

                        throw new IllegalArgumentException(
                                        "Another academic period already uses that name");
                }

                academicPeriod.setName(dto.name());

                academicPeriod.setActive(dto.isActive());

                academicPeriod.setStartDate(dto.startDate());

                academicPeriod.setEndDate(dto.endDate());

                academicPeriodRepository.save(
                                academicPeriod);
        }

        @Transactional
        public void delete(String id) {

                AcademicPeriod academicPeriodToDelete = findById(id);

                if (!academicPeriodToDelete
                                .getStudentAcademicPeriod()
                                .isEmpty()) {

                        throw new IllegalStateException(

                                        "Cannot delete academic period with associated students");
                }

                academicPeriodRepository.delete(
                                academicPeriodToDelete);
        }

        @Transactional(readOnly = true)
        public List<AcademicPeriod> listAllAcademicPeriods() {

                return this.academicPeriodRepository.findAll();
        }

        @Transactional(readOnly = true)
        public List<AcademicPeriod> findAllActivePeriods() {

                return academicPeriodRepository
                                .findByIsActiveTrue();
        }

        @Transactional
        public void activate(String id) {

                AcademicPeriod academicPeriod = findById(id);
                academicPeriod.setActive(true);
        }

        @Transactional
        public void deactivate(String id) {
                AcademicPeriod academicPeriod = findById(id);
                academicPeriod.setActive(false);
        }
=======
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
>>>>>>> origin/features-crud
}
