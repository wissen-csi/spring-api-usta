package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicalTreatmentCreateDTO(
        @NotBlank
        String medicineId,
        @NotBlank 
        String studentId,
        @NotNull 
        LocalDate startMedication,
        @NotNull
        LocalDate endMedication) {

}
