package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        Specialty specialty,
        @NotBlank
        String universityName,
        @NotNull
        LocalDate creationDate,
        @NotNull
        LocalDateTime lastUpdate

) {}