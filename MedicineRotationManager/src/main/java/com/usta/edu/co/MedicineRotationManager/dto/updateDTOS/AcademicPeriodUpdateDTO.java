package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

public record AcademicPeriodUpdateDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {

}