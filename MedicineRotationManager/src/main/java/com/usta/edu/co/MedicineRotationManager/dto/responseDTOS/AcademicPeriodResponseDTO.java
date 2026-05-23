package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import lombok.Builder;
@Builder
public record AcademicPeriodResponseDTO(
      String id,
    String name,
    LocalDate startDate,
    LocalDate endDate
) {

}
