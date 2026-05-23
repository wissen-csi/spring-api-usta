package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;
import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

public record StudentAcademicPeriodUpdateDTO(

    double cumulativeAverage,
    Semester semester,
    AcademicConnection academicConnection

) {
}
