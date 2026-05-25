package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DoctorCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.DoctorResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.DoctorUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Doctor;
import com.usta.edu.co.MedicineRotationManager.services.ServiceDoctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/Doctor")
public class DoctorController {
    private ServiceDoctor serviceDoctor;

    public DoctorController(ServiceDoctor serviceDoctor) {
        this.serviceDoctor = serviceDoctor;
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable String id) {
        Doctor doctor = serviceDoctor.findById(id);
        return ResponseEntity.ok(DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .lastName(doctor.getLastName())
                .dni(doctor.getDni())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .maritalStatus(doctor.getMaritalStatus())
                .typeBlood(doctor.getTypeBlood())
                .weight(doctor.getWeight())
                .imc(doctor.getImc())
                .specialty(doctor.getSpecialty())
                .universityName(doctor.getUniversity() != null ? doctor.getUniversity().getName() : null)
                .universityId(doctor.getUniversity() != null ? doctor.getUniversity().getId() : null)
                .creationDate(doctor.getCreationDate())
                .lastUpdate(doctor.getLastUpdate())
                .placeBirth(new LocationCreateDTO(doctor.getPlaceBirth().getAddress(), doctor.getPlaceBirth().getCity(), doctor.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(doctor.getResidenceAddress().getAddress(), doctor.getResidenceAddress().getCity(), doctor.getResidenceAddress().getDepartment()))
                .build());

    }

    @GetMapping("/find/self")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> findById(@AuthenticationPrincipal AuthUser user) {
        Doctor doctor = serviceDoctor.findById(user.getId());
        return ResponseEntity.ok(DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .lastName(doctor.getLastName())
                .dni(doctor.getDni())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .maritalStatus(doctor.getMaritalStatus())
                .typeBlood(doctor.getTypeBlood())
                .weight(doctor.getWeight())
                .imc(doctor.getImc())
                .specialty(doctor.getSpecialty())
                .universityName(doctor.getUniversity() != null ? doctor.getUniversity().getName() : null)
                .universityId(doctor.getUniversity() != null ? doctor.getUniversity().getId() : null)
                .creationDate(doctor.getCreationDate())
                .lastUpdate(doctor.getLastUpdate())
                .placeBirth(new LocationCreateDTO(doctor.getPlaceBirth().getAddress(), doctor.getPlaceBirth().getCity(), doctor.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(doctor.getResidenceAddress().getAddress(), doctor.getResidenceAddress().getCity(), doctor.getResidenceAddress().getDepartment()))
                .build()

        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> safe(@RequestBody DoctorCreateDTO dto) {
        serviceDoctor.save(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> update(@RequestBody DoctorUpdateDTO dto, @PathVariable String id) {
        serviceDoctor.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/self")
    @PreAuthorize("hasRole('DOCTOR')")

    public ResponseEntity<Void> update(@RequestBody DoctorUpdateDTO dto, @AuthenticationPrincipal AuthUser user) {
        serviceDoctor.update(user.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DoctorResponseDTO>> findAll(Pageable pageable) {
        Page<Doctor> page = serviceDoctor.findAll(pageable);
        Page<DoctorResponseDTO> response = page.map(x -> DoctorResponseDTO.builder()
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
                .specialty(x.getSpecialty())
                .universityName(x.getUniversity() != null ? x.getUniversity().getName() : null)
                .universityId(x.getUniversity() != null ? x.getUniversity().getId() : null)
                .creationDate(x.getCreationDate())
                .lastUpdate(x.getLastUpdate())
                .placeBirth(new LocationCreateDTO(x.getPlaceBirth().getAddress(), x.getPlaceBirth().getCity(), x.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(x.getResidenceAddress().getAddress(), x.getResidenceAddress().getCity(), x.getResidenceAddress().getDepartment()))
                .build());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceDoctor.delete(id);
        return ResponseEntity.noContent().build();
    }

}
