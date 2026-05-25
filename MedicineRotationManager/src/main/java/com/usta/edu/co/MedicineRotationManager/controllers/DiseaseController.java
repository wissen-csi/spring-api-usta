package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.DiseaseResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.Disease;
import com.usta.edu.co.MedicineRotationManager.services.ServiceDisease;

@RestController
@RequestMapping("/api/v1/diseases")
public class DiseaseController {
    private final ServiceDisease serviceDisease;

    public DiseaseController(ServiceDisease serviceDisease) {
        this.serviceDisease = serviceDisease;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody DiseaseCreateDTO dto) {
        serviceDisease.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<DiseaseResponseDTO>> findAll(Pageable pageable) {
        Page<Disease> page = serviceDisease.findAll(pageable);
        Page<DiseaseResponseDTO> response = page.map(serviceDisease::convertToDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DiseaseResponseDTO> findById(@PathVariable String id) {
        Disease disease = serviceDisease.findById(id);
        return ResponseEntity.ok(serviceDisease.convertToDTO(disease));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody DiseaseCreateDTO dto) {
        serviceDisease.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceDisease.delete(id);
        return ResponseEntity.noContent().build();
    }
}
