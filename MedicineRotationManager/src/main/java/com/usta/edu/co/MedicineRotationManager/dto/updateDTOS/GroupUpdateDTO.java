package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GroupUpdateDTO(
        @NotBlank
        String name,
        @Positive
        Integer capacity,
        @NotBlank
        String rotationId
) {
}