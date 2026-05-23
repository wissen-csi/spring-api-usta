package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.*;


public record AcademicPeriodCreateDTO(
        @NotBlank
        String name,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate
) {



}
