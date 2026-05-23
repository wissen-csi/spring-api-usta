package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import jakarta.validation.constraints.NotNull;

public record PorterUpdateDTO(
    @NotNull
    Boolean isActive) {

}
