package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@RequiredArgsConstructor
@Jacksonized
@Builder
public class GroupAssignmentResponseDTO{
    private final String id;
    private final String studentId;
    private final String groupId;
}