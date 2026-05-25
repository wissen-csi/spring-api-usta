package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;

@Builder
public record DiseaseResponseDTO(
    String id,
    String code,
    String name,
    String definition
) {
}
