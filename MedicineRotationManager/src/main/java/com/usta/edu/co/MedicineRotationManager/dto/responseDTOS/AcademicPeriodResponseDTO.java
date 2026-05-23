package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@RequiredArgsConstructor
@Jacksonized
public class AcademicPeriodResponseDTO {
    private final String id;
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;

}
