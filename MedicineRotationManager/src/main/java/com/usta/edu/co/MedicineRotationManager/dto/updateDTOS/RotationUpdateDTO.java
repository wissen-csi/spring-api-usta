package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

import jakarta.validation.constraints.NotNull;

public record RotationUpdateDTO(
        @NotNull
        String doctorId,
        @NotNull
        HospitalLocation hospitalLocation,
        @NotNull
        TypeRotation typeRotation,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate completionDate
) {
}