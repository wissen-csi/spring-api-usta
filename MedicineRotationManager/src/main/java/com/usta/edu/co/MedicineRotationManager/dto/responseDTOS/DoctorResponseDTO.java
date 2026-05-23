package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Getter
@RequiredArgsConstructor
public class DoctorResponseDTO {
    private final String id;
    private final String name;
    private final String lastName;
    private final String dni;
    private final String email;
    private final String phoneNumber;
    private final Specialty specialty;
    private final String universityName;
    private final LocalDate creationDate;
    private final LocalDateTime lastUpdate;
}