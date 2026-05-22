package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record UniversityResponseDTO(

        String id,
        String name,
        String email,
        String phoneNumber,
        boolean isActive,

        String addressId,
        LocalDate creationDate

) {
}