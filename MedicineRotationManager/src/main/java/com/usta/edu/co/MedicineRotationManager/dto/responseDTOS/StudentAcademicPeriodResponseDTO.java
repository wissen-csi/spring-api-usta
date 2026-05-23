package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import lombok.Builder;
@Builder
public record StudentAcademicPeriodResponseDTO(

    String id,
    double cumulativeAverage,
    String studentName,
    String academicPeriodName,
    Semester semester,
    AcademicConnection academicConnection

) {
}
