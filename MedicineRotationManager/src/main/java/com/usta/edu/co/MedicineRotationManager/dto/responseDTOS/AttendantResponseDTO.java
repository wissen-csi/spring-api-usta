package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.TypeAttendant;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class AttendantResponseDTO {

    private final String id;

    private final String name;

    private final String lastName;

    private final String phoneNumber;

    private final String dni;

    private final TypeAttendant typeAttendant;

    private final String studentId;
}