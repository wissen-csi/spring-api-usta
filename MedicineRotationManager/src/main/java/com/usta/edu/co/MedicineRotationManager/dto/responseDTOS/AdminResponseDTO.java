package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
@Builder
public record AdminResponseDTO(
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
        MaritalStatus maritalStatus,
        @NotNull
        TypeBlood typeBlood,
        @Positive
        double weight,
        @Positive
        double imc,
        @NotNull
        LocalDate hiringDate,
        @NotNull
        LocalDate endDate
) {

}
