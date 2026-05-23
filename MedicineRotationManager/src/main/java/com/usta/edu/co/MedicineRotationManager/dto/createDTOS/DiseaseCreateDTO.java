package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record DiseaseCreateDTO( 
    @NotBlank
    String id, 
    @NotBlank
    String code,
    @NotBlank
    String definition, 
    @NotBlank
    String name) {
}

