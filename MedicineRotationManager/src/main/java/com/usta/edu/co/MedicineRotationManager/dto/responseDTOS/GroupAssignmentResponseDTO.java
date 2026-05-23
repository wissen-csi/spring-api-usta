package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import jakarta.validation.constraints.NotBlank;

public record GroupAssignmentResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String studentId,
        @NotBlank
        String groupId

) {
}