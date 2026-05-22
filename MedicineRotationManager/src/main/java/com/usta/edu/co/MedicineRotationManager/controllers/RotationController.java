package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.RotationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.RotationResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.RotationUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.services.ServiceRotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
@RestController
@RequestMapping("/rotation")
public class RotationController {
private  ServiceRotation serviceRotation;

public RotationController(ServiceRotation serviceRotation) {
    this.serviceRotation = serviceRotation;
}
@GetMapping("/find/all")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Page<RotationResponseDTO>> findAll(Pageable pageable){
    Page<Rotation> page = serviceRotation.findAll(pageable);
    Page<RotationResponseDTO> response = page.map(x-> RotationResponseDTO.builder()
    .id(x.getId())
    .doctorId(x.getDoctor().getId())
    .doctorName(x.getDoctor().getName())
    .hospitalLocation(x.getHospitalLocation())
    .typeRotation(x.getTypeRotation())
    .startDate(x.getStartDate())
    .completionDate(x.getCompletionDate())
    .build()
);
    return  ResponseEntity.ok(response);
}
@GetMapping("/find/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<RotationResponseDTO> finById(@PathVariable String id){
    Rotation rotation = serviceRotation.findById(id);
    return ResponseEntity.ok(RotationResponseDTO.builder()
    .id(rotation.getId())
    .doctorId(rotation.getDoctor().getId())
    .doctorName(rotation.getDoctor().getName())
    .hospitalLocation(rotation.getHospitalLocation())
    .typeRotation(rotation.getTypeRotation())
    .startDate(rotation.getStartDate())
    .completionDate(rotation.getCompletionDate())
    .build());

}
@PostMapping("/create/{doctorId}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> create(@RequestBody RotationCreateDTO dto,@PathVariable String doctorId){
    serviceRotation.save(dto, doctorId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
@PostMapping("/create/self")
@PreAuthorize("hasRole('DOCTOR')")

public ResponseEntity<Void> create(@RequestBody RotationCreateDTO dto,@AuthenticationPrincipal AuthUser user){
    serviceRotation.save(dto, user.getId());
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
@PutMapping("/update/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> update(@PathVariable String id, @RequestBody RotationUpdateDTO dto){
    serviceRotation.update(id, dto);
    return ResponseEntity.noContent().build();
}
@DeleteMapping("/update/{id}")
@PreAuthorize("hasRole('ADMIN','DOCTOR')")
public ResponseEntity<Void> delete(@PathVariable String id){
    serviceRotation.delete(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("find/self")
@PreAuthorize("hasRole('DOCTOR')")
public Page<RotationResponseDTO> findByDoctor(@AuthenticationPrincipal AuthUser user, Pageable pageable){
    Page<Rotation> page = serviceRotation.findByDoctor(user.getId(), pageable);
    Page<RotationResponseDTO> response = page.map(x -> RotationResponseDTO.builder()
    .id(x.getId())
    .doctorId(x.getDoctor().getId())
    .doctorName(x.getDoctor().getName())
    .doctorLastName(x.getDoctor().getLastName())
    .hospitalLocation(x.getHospitalLocation())
    .typeRotation(x.getTypeRotation())
    .startDate(x.getStartDate())
    .completionDate(x.getCompletionDate())
    .build()
);
    return response;
}


}
