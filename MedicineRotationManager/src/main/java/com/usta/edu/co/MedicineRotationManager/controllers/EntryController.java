package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.EntryCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.EntryUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Entry;
import com.usta.edu.co.MedicineRotationManager.services.EntryService;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
public class EntryController {
    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('STUDENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody EntryCreateDTO dto) {
        entryService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/qr/{qrCode}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> saveByQr(@PathVariable String qrCode, @RequestBody EntryCreateDTO dto) {
        entryService.saveByQrCode(qrCode, dto.studentId(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PORTER')")
    public ResponseEntity<EntryResponseDTO> findById(@PathVariable String id) {
        Entry entry = entryService.findEntryById(id);
        return ResponseEntity.ok(this.entryService.convertObjectToDTO(entry));
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<Page<EntryResponseDTO>> findAll(Pageable pageable) {
        Page<Entry> page = entryService.findAll(pageable);
        Page<EntryResponseDTO> response = entryService.findAll(pageable).map(entryService::convertObjectToDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        entryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EntryUpdateDTO dto) {
        entryService.update(dto,id);
        return ResponseEntity.noContent().build();
    }

}