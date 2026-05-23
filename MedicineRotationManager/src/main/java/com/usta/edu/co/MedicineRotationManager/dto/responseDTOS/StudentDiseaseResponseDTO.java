package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class StudentDiseaseResponseDTO {

    private final String id;

    private final String studentId;

    private final String diseaseId;

    private final String diseaseCode;

    private final String diseaseName;

    private final Boolean isActive;
}