package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.GroupCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.GroupResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.GroupUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Group;
import com.usta.edu.co.MedicineRotationManager.services.ServiceGroup;

@RestController
@RequestMapping("/Group")

public class GroupController {
    private ServiceGroup serviceGroup;

    public GroupController(ServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Page<GroupResponseDTO>> findAll(Pageable pageable) {
        Page<Group> page = serviceGroup.findAll(pageable);
        Page<GroupResponseDTO> response = page.map(x -> GroupResponseDTO.builder()
                .id(x.getId())
                .name(x.getName())
                .capacity(x.getCapacity())
                .rotationId(x.getRotation().getId())
                .build()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<GroupResponseDTO> findById(@PathVariable String id) {
        Group group = serviceGroup.findById(id);
        return ResponseEntity.ok(GroupResponseDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .capacity(group.getCapacity())
                .rotationId(group.getRotation().getId())
                .build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> create(@RequestBody GroupCreateDTO dto) {
        serviceGroup.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceGroup.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody GroupUpdateDTO dto) {
        serviceGroup.update(id, dto);
        return ResponseEntity.noContent().build();
    }
}
