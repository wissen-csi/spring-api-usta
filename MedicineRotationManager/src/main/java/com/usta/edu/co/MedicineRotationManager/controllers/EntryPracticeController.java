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

import com.usta.edu.co.MedicineRotationManager.dto.EntryPracticeCreationDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryPracticeResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.services.ServiceEntryPractice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("entry/practice")
public class EntryPracticeController {
    private ServiceEntryPractice serviceEntryPractice;

    public EntryPracticeController(ServiceEntryPractice serviceEntryPractice) {
        this.serviceEntryPractice = serviceEntryPractice;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> save(@RequestBody EntryPracticeCreationDTO dto) {
        serviceEntryPractice.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceEntryPractice.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN','DOCTOR','STUDENT')")
    public ResponseEntity<Page<EntryPracticeResponseDTO>> findAll(Pageable pageable) {
        Page<EntryPractice> page = serviceEntryPractice.findAll(pageable);
        Page<EntryPracticeResponseDTO> response = page.map(x -> EntryPracticeResponseDTO.builder()
                .id(x.getId())
                .startTime(x.getStartTime())
                .endTime(x.getEndTime())
                .groupName(x.getGroup().getName())
                .groupId(x.getGroup().getId())
                .qrCode(x.getQrCode())
                .build()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN','DOCTOR','STUDENT')")
    public ResponseEntity<EntryPracticeResponseDTO> findById(@PathVariable String id) {
        EntryPractice entryPractice = serviceEntryPractice.findById(id);
        return ResponseEntity.ok(EntryPracticeResponseDTO.builder()
                .id(entryPractice.getId())
                .startTime(entryPractice.getStartTime())
                .endTime(entryPractice.getEndTime())
                .groupName(entryPractice.getGroup().getName())
                .groupId(entryPractice.getGroup().getId())
                .qrCode(entryPractice.getQrCode())
                .build());
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EntryPracticeCreationDTO dto) {
        serviceEntryPractice.update(id, dto);
        return ResponseEntity.noContent().build();
    }


}
