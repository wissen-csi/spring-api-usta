package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import jakarta.validation.constraints.NotBlank;

public record MedicineUpdateDTO(
    @NotBlank
    String activeIngredient, 
    @NotBlank
    String descriptonAtc) {

}
