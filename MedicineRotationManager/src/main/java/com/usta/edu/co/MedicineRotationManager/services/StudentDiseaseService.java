package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCieDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentDiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.StudentDiseaseResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentDiseaseUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Disease;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.models.StudentDisease;
import com.usta.edu.co.MedicineRotationManager.repositories.DiseaseRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentDiseaseRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentDiseaseService {

    private final StudentDiseaseRepository studentDiseaseRepository;

    private final DiseaseRepository diseaseRepository;

    private final ServiceStudent studentService;

    private final CieService cieService;

    /*
     * SAVE
     */
    @Transactional
    public void save(
            StudentDiseaseCreateDTO dto
    ) {

        /*
         * FIND STUDENT
         */
        /*
         * FIND STUDENT BY DNI (frontend sends DNI)
         */
        Student student =
                studentService.findByDni(
                        dto.studentId()
                );

        /*
         * FIND OR CREATE DISEASE FROM CIE DATA
         */
        DiseaseCieDTO cieData = dto.diseaseCieDTO();

        Disease disease =
                diseaseRepository
                        .findByCode(
                                cieData.code()
                        )
                        .orElseGet(() -> {

                            Disease newDisease =
                                    new Disease();

                            newDisease.setId(
                                    cieData.fundationURI()
                            );

                            newDisease.setCode(
                                    cieData.code()
                            );

                            newDisease.setName(
                                    cieData.label()
                            );

                            newDisease.setDefinition(
                                    cieData.label()
                            );

                            return diseaseRepository.save(
                                    newDisease
                            );
                        });

        /*
         * CREATE RELATION
         */
        StudentDisease studentDisease =
                new StudentDisease();

        studentDisease.setId(
                UUIDGenerator.generateNewId()
        );

        studentDisease.setStudent(student);

        studentDisease.setDisease(disease);

        studentDisease.setActive(
                dto.isActive()
        );

        studentDiseaseRepository.save(
                studentDisease
        );
    }

    /*
     * FIND ALL
     */
    public Page<StudentDisease> findAll(
            Pageable pageable
    ) {

        return studentDiseaseRepository.findAll(
                pageable
        );
    }

    /*
     * FIND BY ID
     */
    public StudentDisease findById(
            String id
    ) {

        return studentDiseaseRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Student disease not found"
                        )
                );
    }

    /*
     * UPDATE
     */
    @Transactional
    public void update(

            StudentDiseaseUpdateDTO dto,

            String id
    ) {

        StudentDisease studentDisease =
                findById(id);

        /*
         * UPDATE ACTIVE STATUS
         */
        if (dto.isActive() != null) {

            studentDisease.setActive(
                    dto.isActive()
            );
        }

        /*
         * UPDATE STUDENT
         */
        if (dto.studentId() != null) {

            Student student =
                    studentService.findByDni(
                            dto.studentId()
                    );

            studentDisease.setStudent(student);
        }

        /*
         * UPDATE DISEASE
         */
        if (dto.diseaseCieDTO() != null) {

            DiseaseCreateDTO diseaseData =
                    cieService.searchSpecificDiaseasse(
                            dto.diseaseCieDTO()
                    );

            Disease disease =
                    diseaseRepository
                            .findByCode(
                                    diseaseData.code()
                            )
                            .orElseGet(() -> {

                                Disease newDisease =
                                        new Disease();

                                newDisease.setId(
                                        diseaseData.id()
                                );

                                newDisease.setCode(
                                        diseaseData.code()
                                );

                                newDisease.setName(
                                        diseaseData.name()
                                );

                                newDisease.setDefinition(
                                        diseaseData.definition()
                                );

                                return diseaseRepository.save(
                                        newDisease
                                );
                            });

            studentDisease.setDisease(
                    disease
            );
        }

        studentDiseaseRepository.save(studentDisease);
    }

    /*
     * DELETE
     */
    public void delete(
            String id
    ) {

        StudentDisease studentDisease =
                findById(id);

        studentDiseaseRepository.delete(
                studentDisease
        );
    }


    public StudentDiseaseResponseDTO convertObjectToDTO(StudentDisease studentDisease) {

        return StudentDiseaseResponseDTO.builder()

                .id(studentDisease.getId())

                .studentId(
                        studentDisease.getStudent().getId()
                )

                .diseaseId(
                        studentDisease.getDisease().getId()
                )

                .diseaseCode(
                        studentDisease.getDisease().getCode()
                )

                .diseaseName(
                        studentDisease.getDisease().getName()
                )

                .isActive(studentDisease.isActive()
                )

                .build();
    }
}


