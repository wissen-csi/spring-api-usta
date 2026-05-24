package com.usta.edu.co.MedicineRotationManager.controllers;


import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.PorterCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.PorterResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.PorterUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Porter;
import com.usta.edu.co.MedicineRotationManager.services.PorterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/porters")
public class PorterController {

    private final PorterService porterService;

    public PorterController(PorterService porterService) {
        this.porterService = porterService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody PorterCreateDTO dto) {
        porterService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<Page<PorterResponseDTO>> findAll(Pageable pageable) {

        Page<PorterResponseDTO> response = porterService.findAll(pageable)
                        .map(porterService::convertObjectToResponseDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<PorterResponseDTO> findById(@PathVariable String id) {

        Porter porter = porterService.findById(id);

        return ResponseEntity.ok(porterService.convertObjectToResponseDTO(porter));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody PorterUpdateDTO dto) {
        porterService.update(dto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        porterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



