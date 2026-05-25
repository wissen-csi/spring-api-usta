package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record MedicineCreateDTO(
    @NotBlank
    String name,
    @NotBlank
    String gramaje) {

}
