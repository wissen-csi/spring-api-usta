package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;


public record GroupUpdateDTO(
        String name,
        Integer capacity,
        String rotationId
) {
}