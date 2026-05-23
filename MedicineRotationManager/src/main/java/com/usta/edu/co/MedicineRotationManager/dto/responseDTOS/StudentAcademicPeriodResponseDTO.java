package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
@Builder
public record StudentAcademicPeriodResponseDTO(
    @NotBlank
    String id,
    @Positive
    double cumulativeAverage,
    @NotBlank
    String studentName,
    @NotBlank
    String academicPeriodName,
    @NotNull
    Semester semester,
    @NotNull
    AcademicConnection academicConnection

) {
}
