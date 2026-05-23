package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UniversityResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotNull
        boolean isActive,
        @NotBlank
        String addressId,
        @NotNull
        LocalDate creationDate

) {
}