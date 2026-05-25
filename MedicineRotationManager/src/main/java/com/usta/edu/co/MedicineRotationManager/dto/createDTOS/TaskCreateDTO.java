package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record TaskCreateDTO(
    @NotBlank
    String description,
    @NotBlank
    String adminId) {
}
