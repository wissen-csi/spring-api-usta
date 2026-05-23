package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudentAcademicPeriodCreateDTO(
    @Positive
    double cumulativeAverage,
    @NotBlank
    String studentId,
    @NotBlank
    String academicPeriodId,
    @NotNull
    Semester semester,
    @NotNull
    AcademicConnection academicConnection

) {
}