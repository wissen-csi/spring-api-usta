package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record RotationResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String doctorId,
        @NotBlank
        String doctorName,
        @NotBlank
        String doctorLastName,
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