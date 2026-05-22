package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AdminCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.AdminResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.Admin;
import com.usta.edu.co.MedicineRotationManager.services.ServiceAdmin;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ServiceAdmin serviceAdmin;

    public AdminController(ServiceAdmin serviceAdmin) {

        this.serviceAdmin = serviceAdmin;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody AdminCreateDTO dto) {
        serviceAdmin.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminResponseDTO>> finAll(Pageable pageable) {
        Page<Admin> modelsResponse = serviceAdmin.findAll(pageable);
        Page<AdminResponseDTO> dtoPage = modelsResponse.map(x -> AdminResponseDTO.builder()
                .id(x.getId())
                .name(x.getName())
                .lastName(x.getLastName())
                .dni(x.getDni())
                .email(x.getEmail())
                .phoneNumber(x.getPhoneNumber())
                .maritalStatus(x.getMaritalStatus())
                .typeBlood(x.getTypeBlood())
                .weight(x.getWeight())
                .imc(x.getImc())
                .hiringDate(x.getHiringDate())
                .endDate(x.getEndDate())
                .build());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDTO> findById(@PathVariable String id) {
        Admin admin = serviceAdmin.findById(id);

        AdminResponseDTO response = AdminResponseDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .lastName(admin.getLastName())
                .dni(admin.getDni())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .maritalStatus(admin.getMaritalStatus())
                .typeBlood(admin.getTypeBlood())
                .weight(admin.getWeight())
                .imc(admin.getImc())
                .hiringDate(admin.getHiringDate())
                .endDate(admin.getEndDate())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable String id){
        serviceAdmin.delete(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    

}