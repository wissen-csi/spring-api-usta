package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class MedicineResponseDTO {

    private final String id;
    private final String name;
    private final String gramaje;
    private final String activeIngredient;
    private final String description;
}