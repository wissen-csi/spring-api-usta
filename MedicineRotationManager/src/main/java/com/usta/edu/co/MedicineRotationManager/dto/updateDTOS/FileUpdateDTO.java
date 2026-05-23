package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import jakarta.validation.constraints.NotBlank;

public record FileUpdateDTO(
    @NotBlank
    String originalName) {

}
