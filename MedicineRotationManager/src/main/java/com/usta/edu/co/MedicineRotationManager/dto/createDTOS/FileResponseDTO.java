package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record FileResponseDTO( 
    @NotBlank
    String id,
    @NotBlank
    String secureUrl,
    @NotBlank
    String originalName) {
}
