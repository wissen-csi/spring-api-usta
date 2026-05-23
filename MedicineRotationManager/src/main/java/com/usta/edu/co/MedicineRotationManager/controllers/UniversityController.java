package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.UniversityCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.UniversityResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.UniversityUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.services.ServiceUniversity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/university")
public class UniversityController {
    private ServiceUniversity serviceUniversity;

    public UniversityController(ServiceUniversity serviceUniversity) {
        this.serviceUniversity = serviceUniversity;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody UniversityCreateDTO dto){
        serviceUniversity.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id){
        serviceUniversity.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDelete(@PathVariable String id){
        serviceUniversity.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restore(@PathVariable String id){
        serviceUniversity.restore(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UniversityResponseDTO>> findAll(Pageable pageable){
        Page<University> page = serviceUniversity.findAll(pageable);
        Page<UniversityResponseDTO> response = page.map(x->UniversityResponseDTO.builder()
    .id(x.getId())
    .name(x.getName())
    .email(x.getEmail())
    .phoneNumber(x.getPhoneNumber())
    .isActive(x.isActive())
    .addressId(x.getAddress().getId())
    .creationDate(x.getCreationDate())
    .build()
    );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UniversityResponseDTO> findById(@PathVariable String id){
        University university = serviceUniversity.findById(id);
        return ResponseEntity.ok(UniversityResponseDTO.builder()
    .id(university.getId())
    .name(university.getName())
    .email(university.getEmail())
    .phoneNumber(university.getPhoneNumber())
    .isActive(university.isActive())
    .addressId(university.getAddress().getId())
    .creationDate(university.getCreationDate())
    .build()
    );
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@RequestBody UniversityUpdateDTO dto, @PathVariable String id){
        serviceUniversity.update(id, dto);
        return ResponseEntity.noContent().build();
    }
}
