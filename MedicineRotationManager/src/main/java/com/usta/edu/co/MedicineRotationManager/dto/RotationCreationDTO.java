package com.usta.edu.co.MedicineRotationManager.dto;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

public record RotationCreationDTO(
    String doctorId,
    HospitalLocation hospitalLocation,
    TypeRotation typeRotation,
    LocalDate startDate,
    LocalDate completionDate,
    int capacity
) {

}
