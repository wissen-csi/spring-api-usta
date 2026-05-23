package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

public record RotationCreateDTO(
    HospitalLocation hospitalLocation,
    TypeRotation typeRotation,
    LocalDate startDate,
    LocalDate completionDate
) {

}
