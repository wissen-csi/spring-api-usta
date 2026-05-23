package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

public record StudentAcademicPeriodCreateDTO(

    double cumulativeAverage,
    String studentId,
    String academicPeriodId,
    Semester semester,
    AcademicConnection academicConnection

) {
}