package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@RequiredArgsConstructor
@Jacksonized
@Getter
public class RotationResponseDTO{
    private final String id;
    private final String doctorId;
    private final String doctorName;
    private final String doctorLastName;
    private final HospitalLocation hospitalLocation;
    private final TypeRotation typeRotation;
    private final LocalDate startDate;
    private final LocalDate completionDate;
}