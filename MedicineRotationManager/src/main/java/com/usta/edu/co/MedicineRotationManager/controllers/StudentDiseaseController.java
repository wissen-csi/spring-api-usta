package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentDiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.StudentDiseaseResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentDiseaseUpdateDTO;

import com.usta.edu.co.MedicineRotationManager.models.StudentDisease;

import com.usta.edu.co.MedicineRotationManager.services.StudentDiseaseService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-diseases")
public class StudentDiseaseController {

    private final StudentDiseaseService studentDiseaseService;

    public StudentDiseaseController(
            StudentDiseaseService studentDiseaseService
    ) {

        this.studentDiseaseService = studentDiseaseService;
    }

    /*
     * CREATE RELATION
     */
    @PostMapping
    @PreAuthorize(
            "hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('STUDENT')"
    )
    public ResponseEntity<Void> save(@RequestBody StudentDiseaseCreateDTO studentDiseaseCreateDTO) {

        this.studentDiseaseService.save(
                studentDiseaseCreateDTO
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
     * FIND ALL
     */
    @GetMapping
    @PreAuthorize(
            "hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('STUDENT')"
    )
    public ResponseEntity<Page<StudentDiseaseResponseDTO>> findAll(
            Pageable pageable
    ) {

        Page<StudentDisease> studentDiseases =
                this.studentDiseaseService.findAll(
                        pageable
                );

        Page<StudentDiseaseResponseDTO> response =
                studentDiseases.map(
                        studentDiseaseService::convertObjectToDTO
                );

        return ResponseEntity.ok(
                response
        );
    }

    /*
     * FIND BY ID
     */
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('DOCTOR') or hasRole('ADMIN')"
    )
    public ResponseEntity<StudentDiseaseResponseDTO> findById(@PathVariable String id) {

        StudentDisease studentDisease =
                this.studentDiseaseService.findById(
                        id
                );

        return ResponseEntity.ok(
                this.studentDiseaseService
                        .convertObjectToDTO(studentDisease)
        );
    }

    /*
     * UPDATE
     */
    @PutMapping("/{id}")
    @PreAuthorize(
            "hasRole('DOCTOR') or hasRole('ADMIN')"
    )
    public ResponseEntity<Void> update(

            @PathVariable String id,

            @RequestBody
                    StudentDiseaseUpdateDTO studentDiseaseUpdateDTO
    ) {

        this.studentDiseaseService.update(
                studentDiseaseUpdateDTO,
                id
        );

        return ResponseEntity.noContent().build();
    }

    /*
     * DELETE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        this.studentDiseaseService.delete(
                id);

        return ResponseEntity.noContent().build();
    }
}