package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@RequiredArgsConstructor
@Jacksonized
public class StudentAcademicPeriodResponseDTO {
    private final String id;
    private final double cumulativeAverage;
    private final String studentName;
    private final String academicPeriodName;
    private final Semester semester;
    private final AcademicConnection academicConnection;
}
