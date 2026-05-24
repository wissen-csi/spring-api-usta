package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DoctorResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        String dni,
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        MaritalStatus maritalStatus,
        TypeBlood typeBlood,
        Double weight,
        Double imc,
        Specialty specialty,
        String universityName,
        String universityId,
        LocalDate creationDate,
        LocalDateTime lastUpdate,
        LocationCreateDTO placeBirth,
        LocationCreateDTO residenceAddress

) {}

