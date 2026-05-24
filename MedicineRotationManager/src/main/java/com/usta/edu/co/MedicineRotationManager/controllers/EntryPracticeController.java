package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.EntryPracticeCreationDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryPracticeResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
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
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> save(@RequestBody EntryPracticeCreationDTO dto) {
        serviceEntryPractice.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceEntryPractice.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STUDENT')")
    public ResponseEntity<Page<EntryPracticeResponseDTO>> findAll(Pageable pageable, @AuthenticationPrincipal AuthUser user) {
        boolean isStudent = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        Page<EntryPractice> page = serviceEntryPractice.findAll(pageable);
        Page<EntryPracticeResponseDTO> response = page.map(x -> EntryPracticeResponseDTO.builder()
                .id(isStudent ? null : x.getId())
                .title(x.getTitle())
                .startTime(x.getStartTime())
                .endTime(x.getEndTime())
                .groupName(x.getGroup().getName())
                .groupId(isStudent ? null : x.getGroup().getId())
                .qrCode(x.getQrCode())
                .build()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/self")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Page<EntryPracticeResponseDTO>> findSelf(Pageable pageable, @AuthenticationPrincipal AuthUser user) {
        Page<EntryPractice> page = serviceEntryPractice.findByDoctorId(user.getId(), pageable);
        Page<EntryPracticeResponseDTO> response = page.map(x -> EntryPracticeResponseDTO.builder()
                .id(x.getId())
                .title(x.getTitle())
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
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STUDENT')")
    public ResponseEntity<EntryPracticeResponseDTO> findById(@PathVariable String id, @AuthenticationPrincipal AuthUser user) {
        boolean isStudent = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        EntryPractice entryPractice = serviceEntryPractice.findById(id);
        return ResponseEntity.ok(EntryPracticeResponseDTO.builder()
                .id(isStudent ? null : entryPractice.getId())
                .title(entryPractice.getTitle())
                .startTime(entryPractice.getStartTime())
                .endTime(entryPractice.getEndTime())
                .groupName(entryPractice.getGroup().getName())
                .groupId(isStudent ? null : entryPractice.getGroup().getId())
                .qrCode(entryPractice.getQrCode())
                .build());
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EntryPracticeCreationDTO dto) {
        serviceEntryPractice.update(id, dto);
        return ResponseEntity.noContent().build();
    }


}
