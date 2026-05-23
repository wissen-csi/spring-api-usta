package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.MedicalTreatmentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.MedicalTreatmentResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.MedicalTreatmentUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.MedicalTreatment;
import com.usta.edu.co.MedicineRotationManager.services.MedicalTreatmentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical-treatments")
public class MedicalTreatmentController {

    private final MedicalTreatmentService medicalTreatmentService;

    public MedicalTreatmentController(MedicalTreatmentService medicalTreatmentService) {
        this.medicalTreatmentService = medicalTreatmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> save(@RequestBody MedicalTreatmentCreateDTO medicalTreatmentCreateDTO) {

        this.medicalTreatmentService.save(
                medicalTreatmentCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<Page<MedicalTreatmentResponseDTO>> findAll(Pageable pageable) {

        Page<MedicalTreatment> medicalTreatmentPage =
                this.medicalTreatmentService.findAll(pageable);

        Page<MedicalTreatmentResponseDTO> response =
                medicalTreatmentPage.map(
                        medicalTreatmentService::convertObjectToDTO
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<MedicalTreatmentResponseDTO> findById(
            @PathVariable String id) {

        MedicalTreatment medicalTreatment =
                this.medicalTreatmentService.findById(id);

        return ResponseEntity.ok(
                this.medicalTreatmentService
                        .convertObjectToDTO(medicalTreatment)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> update(

            @PathVariable String id,

            @RequestBody
                    MedicalTreatmentUpdateDTO medicalTreatmentUpdateDTO
    ) {

        this.medicalTreatmentService.update(
                medicalTreatmentUpdateDTO,
                id
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        this.medicalTreatmentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
