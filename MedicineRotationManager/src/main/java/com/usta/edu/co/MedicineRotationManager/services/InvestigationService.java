
package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.InvestigationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.InvestigationUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Investigation;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.InvestigationRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.DateValidator;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvestigationService {

    private final InvestigationRepository investigationRepository;

    private final ServiceStudent serviceStudent;

    @Transactional
    public void save(
            InvestigationCreateDTO dto) {

        Student student = findStudentById(dto.studentId());

        DateValidator.validateNotFutureDate(
                dto.publicationDate());

        Investigation investigation = Investigation.builder()
                .id(UUIDGenerator.generateNewId())
                .repositoryUrl(dto.repositoryUrl())
                .description(dto.description())
                .publicationDate(dto.publicationDate())
                .student(student)
                .build();

        investigationRepository.save(investigation);
    }

    @Transactional
    public void update(
            InvestigationUpdateDTO dto,
            String id) {

        Investigation investigation = findInvestigationById(id);

        DateValidator.validateNotFutureDate(
                dto.publicationLocalDate());

        investigation.setRepositoryUrl(
                dto.repositoryUrl());

        investigation.setDescription(
                dto.description());

        investigation.setPublicationDate(
                dto.publicationLocalDate());

        investigationRepository.save(investigation);
    }

    @Transactional
    public void delete(String id) {

        Investigation investigation = findInvestigationById(id);

        investigationRepository.delete(investigation);
    }

    @Transactional(readOnly = true)
    public Investigation findInvestigationById(
            String id) {

        return investigationRepository.findById(id)

                .orElseThrow(() ->

                new EntityNotFoundException(
                        "Investigation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Student findStudentById(String id) {
        return this.serviceStudent.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Investigation> listAll(
            Pageable pageable) {

        return investigationRepository.findAll(pageable);
    }
}
