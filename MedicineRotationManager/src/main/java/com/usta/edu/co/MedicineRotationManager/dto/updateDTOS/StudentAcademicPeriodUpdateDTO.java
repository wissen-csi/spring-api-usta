package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;
import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudentAcademicPeriodUpdateDTO(
    @Positive
    double cumulativeAverage,
    @NotNull
    Semester semester,
    @NotNull
    AcademicConnection academicConnection

) {
}
