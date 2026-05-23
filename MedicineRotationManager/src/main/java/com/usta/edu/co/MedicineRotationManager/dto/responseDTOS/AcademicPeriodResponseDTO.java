package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record AcademicPeriodResponseDTO(
    @NotBlank
    String id,
    @NotBlank
    String name,
    @NotNull
    LocalDate startDate,
    @NotNull
    LocalDate endDate
) {
}
