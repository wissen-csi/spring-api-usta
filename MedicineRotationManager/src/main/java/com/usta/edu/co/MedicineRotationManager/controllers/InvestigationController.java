package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.InvestigationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.InvestigationResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.InvestigationUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Investigation;
import com.usta.edu.co.MedicineRotationManager.services.InvestigationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;

    public InvestigationController(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> save(@RequestBody InvestigationCreateDTO investigationCreateDTO) {

        this.investigationService.save(investigationCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<InvestigationResponseDTO>> findAll(Pageable pageable) {

        Page<Investigation> pageInvestigations =
                this.investigationService.listAll(pageable);

        Page<InvestigationResponseDTO> response =
                pageInvestigations.map(
                        investigationService::convertObjectToDTO
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<InvestigationResponseDTO> findById(
            @PathVariable String id
    ) {

        Investigation investigation =
                this.investigationService.findInvestigationById(id);

        return ResponseEntity.ok(
                this.investigationService
                        .convertObjectToDTO(investigation)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {

        this.investigationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> update(@RequestBody InvestigationUpdateDTO investigationUpdateDTO, @PathVariable String id) {

        this.investigationService.update(
                investigationUpdateDTO,
                id);

        return ResponseEntity.noContent().build();
    }
}


