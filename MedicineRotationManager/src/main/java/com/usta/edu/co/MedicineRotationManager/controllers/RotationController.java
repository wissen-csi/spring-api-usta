package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.RotationResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.services.ServiceRotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/rotation")
public class RotationController {
private  ServiceRotation serviceRotation;

public RotationController(ServiceRotation serviceRotation) {
    this.serviceRotation = serviceRotation;
}
@GetMapping("/find/all")
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

}
