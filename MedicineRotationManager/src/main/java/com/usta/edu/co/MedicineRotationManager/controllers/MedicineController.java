package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.MedicineCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.MedicineResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.MedicineUpdateDTO;

import com.usta.edu.co.MedicineRotationManager.models.Medicine;

import com.usta.edu.co.MedicineRotationManager.services.MedicineService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> save(@RequestBody MedicineCreateDTO medicineCreateDTO) {

        this.medicineService.save(medicineCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<Page<MedicineResponseDTO>> findAll(Pageable pageable) {

        Page<Medicine> medicines =
                this.medicineService.findAll(pageable);

        Page<MedicineResponseDTO> response =
                medicines.map(
                        medicineService::convertObjectToDTO
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<MedicineResponseDTO> findById(@PathVariable String id) {

        Medicine medicine = this.medicineService.findById(id);

        return ResponseEntity.ok(
                this.medicineService
                        .convertObjectToDTO(medicine)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody
            MedicineUpdateDTO medicineUpdateDTO) {

        this.medicineService.update(medicineUpdateDTO, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}