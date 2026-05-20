package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
public record DoctorResponseDTO(

        String id,

        String name,
        String lastName,
        String dni,
        String email,
        String phoneNumber,

        Specialty specialty,

        String universityName,

        LocalDate creationDate,
        LocalDateTime lastUpdate

) {}