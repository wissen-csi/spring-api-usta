package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;

@Builder
public record GroupResponseDTO(String id, String name, int capacity, String rotationId) {

}