package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.AcademicPeriodResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.services.AcademicPeriodService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/academic/period")
public class AcademicPeriodController {
    private AcademicPeriodService serviceAcademicPeriod;

    public AcademicPeriodController(AcademicPeriodService serviceAcademicPeriod) {
        this.serviceAcademicPeriod = serviceAcademicPeriod;
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN','STUDENT','DOCTOR')")
    public ResponseEntity<Page<AcademicPeriodResponseDTO>> findAll(Pageable pageable) {
        Page<AcademicPeriod> page = serviceAcademicPeriod.findAll(pageable);
        Page<AcademicPeriodResponseDTO> response = page.map(x -> AcademicPeriodResponseDTO.builder()
                .id(x.getId())
                .name(x.getName())
                .startDate(x.getStartDate())
                .endDate(x.getEndDate())
                .build()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN','STUDENT','DOCTOR')")
    public ResponseEntity<AcademicPeriodResponseDTO> findById(@PathVariable String id) {
        AcademicPeriod academicPeriod = serviceAcademicPeriod.findById(id);
        return ResponseEntity.ok(AcademicPeriodResponseDTO.builder()
                .id(academicPeriod.getId())
                .name(academicPeriod.getName())
                .startDate(academicPeriod.getStartDate())
                .endDate(academicPeriod.getEndDate())
                .build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody AcademicPeriodCreateDTO dto) {
        serviceAcademicPeriod.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody AcademicPeriodUpdateDTO dto) {
        serviceAcademicPeriod.update(dto,id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceAcademicPeriod.delete(id);
        return ResponseEntity.noContent().build();
    }


}
