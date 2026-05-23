package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

import lombok.Builder;
@Builder
public record RotationResponseDTO(
        String id,
        String doctorId,
        String doctorName,
        String doctorLastName,
        HospitalLocation hospitalLocation,
        TypeRotation typeRotation,
        LocalDate startDate,
        LocalDate completionDate
) {
}