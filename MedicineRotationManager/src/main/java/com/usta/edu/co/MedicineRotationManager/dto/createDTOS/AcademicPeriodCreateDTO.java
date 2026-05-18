package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

public record AcademicPeriodCreateDTO(String name, LocalDate startDate, LocalDate endDate, boolean isActive) {

}
