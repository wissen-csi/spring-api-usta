package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GroupCreateDTO(
    @NotBlank
    String name,
    @NotBlank 
    String rotationId,
    @Positive 
    int capacity) {
    
}
