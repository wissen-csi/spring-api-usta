package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record MedicalTreatmentUpdateDTO(
    @NotNull
    LocalDate startMedication, 
    @NotNull
    LocalDate endMedication) {

}
