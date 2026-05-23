package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;


import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record EntryPracticeResponseDTO(
    @NotBlank
    String id,
    @NotNull
    LocalDateTime startTime,
    @NotNull
    LocalDateTime endTime,
    @NotBlank
    String groupName,
    @NotBlank
    String groupId,
    @NotBlank
    String qrCode

) {
}