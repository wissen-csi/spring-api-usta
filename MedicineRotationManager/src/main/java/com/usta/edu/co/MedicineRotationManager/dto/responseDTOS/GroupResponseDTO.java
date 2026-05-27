package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@RequiredArgsConstructor
@Jacksonized
@Getter
public class GroupResponseDTO {
    private final String id;
    private final String name;
    private final int capicity;
    private final String rotationId;

}
