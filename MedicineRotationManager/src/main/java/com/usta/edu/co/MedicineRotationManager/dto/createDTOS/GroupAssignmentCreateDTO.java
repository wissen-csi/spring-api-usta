package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record GroupAssignmentCreateDTO(
    @NotBlank
    String idStudent,
    @NotBlank 
    String idGroup) {
}
