
package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.repositories.AcademicPeriodRepository;
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
}
