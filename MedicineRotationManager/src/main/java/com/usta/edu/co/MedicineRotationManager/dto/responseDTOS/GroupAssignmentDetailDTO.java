package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record GroupAssignmentDetailDTO(
    String assignmentId,
    String studentId,
    String studentName,
    String studentDni,
    String groupId,
    String groupName,
    int capacity,
    String rotationId,
    String rotationType,
    String hospitalLocation,
    LocalDate startDate,
    LocalDate completionDate,
    String doctorName
) {
}
