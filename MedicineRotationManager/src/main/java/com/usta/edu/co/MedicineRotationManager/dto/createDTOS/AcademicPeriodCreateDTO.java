package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

<<<<<<< HEAD
public record AcademicPeriodCreateDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
=======
public record AcademicPeriodCreateDTO(String name, LocalDate startDate, LocalDate endDate, boolean isActive) {
>>>>>>> origin/features-roles-jwt

}
