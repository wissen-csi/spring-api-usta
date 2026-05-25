package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;


import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder

@Jacksonized
@Getter
@RequiredArgsConstructor


public class EntryPracticeResponseDTO {
    private final String id;
    private final String title;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String groupName;
    private final String groupId;
    private final String qrCode;

    public static record FileResponseDTO(String id,
                                         String secureUrl,
                                         String originalName,
                                         String personId) {
    }
}