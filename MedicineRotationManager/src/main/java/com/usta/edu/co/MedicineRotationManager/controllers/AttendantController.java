package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AttendantCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.AttendantResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AttendantUpdateDTO;

import com.usta.edu.co.MedicineRotationManager.models.Attendant;

import com.usta.edu.co.MedicineRotationManager.services.AttendantService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendants")
public class AttendantController {

    private final AttendantService attendantService;

    public AttendantController(
            AttendantService attendantService
    ) {

        this.attendantService = attendantService;
    }

    /*
     * CREATE
     */
    @PostMapping
    @PreAuthorize(
            "hasRole('STUDENT') or hasRole('ADMIN')"
    )
    public ResponseEntity<Void> save(

            @RequestBody
                    AttendantCreateDTO attendantCreateDTO
    ) {

        this.attendantService.saveAttendant(
                attendantCreateDTO
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    /*
     * FIND ALL
     */
    @GetMapping
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<Page<AttendantResponseDTO>> findAll(
            Pageable pageable
    ) {

        Page<Attendant> attendants =
                this.attendantService.findAllAttendandts(
                        pageable
                );

        Page<AttendantResponseDTO> response =
                attendants.map(
                        attendantService::convertObjectToDTO
                );

        return ResponseEntity.ok(
                response
        );
    }

    /*
     * FIND BY ID
     */
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or hasRole('DOCTOR')"
    )
    public ResponseEntity<AttendantResponseDTO> findById(
            @PathVariable String id
    ) {

        Attendant attendant =
                this.attendantService.findById(
                        id
                );

        return ResponseEntity.ok(
                this.attendantService
                        .convertObjectToDTO(attendant)
        );
    }

    /*
     * UPDATE
     */
    @PutMapping("/{id}")
    @PreAuthorize(
            "hasRole('STUDENT') or hasRole('ADMIN')"
    )
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody AttendantUpdateDTO attendantUpdateDTO) {

        this.attendantService.update(
                attendantUpdateDTO,
                id
        );

        return ResponseEntity.noContent().build();
    }

    /*
     * DELETE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        this.attendantService.delete(id);

        return ResponseEntity.noContent().build();
    }
}