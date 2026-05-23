package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@RequiredArgsConstructor
public class UniversityResponseDTO{
    private final String id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final boolean isActive;
    private final String addressId;
    private final LocalDate creationDate;
}