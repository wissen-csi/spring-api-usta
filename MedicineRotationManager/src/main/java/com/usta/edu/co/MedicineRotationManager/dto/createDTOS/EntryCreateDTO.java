package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public record EntryCreateDTO(
    @NotNull
    LocalDateTime assitance, 
    @NotBlank
    String studentId) {

}
