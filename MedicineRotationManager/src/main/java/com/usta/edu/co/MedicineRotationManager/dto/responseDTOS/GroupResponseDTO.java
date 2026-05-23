package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record GroupResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @Positive
        int capacity,
        @NotBlank
        String rotationId
) {
}

