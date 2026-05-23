package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;


import java.time.LocalDateTime;

import lombok.Builder;
@Builder
public record EntryPracticeResponseDTO(

    String id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String groupName,
    String groupId,
    String qrCode

) {
}