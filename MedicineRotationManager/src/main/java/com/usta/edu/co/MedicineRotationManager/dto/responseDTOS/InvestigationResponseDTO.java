package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class InvestigationResponseDTO {
    private final String id;
    private final String repositoryUrl;
    private final String description;
    private final LocalDate publicationDate;
    private final String studentId;


}