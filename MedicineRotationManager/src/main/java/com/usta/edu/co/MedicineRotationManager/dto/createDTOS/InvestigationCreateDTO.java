package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record InvestigationCreateDTO(
        @NotBlank
        String repositoryUrl, 
        @NotBlank
        String description, 
        @NotNull
        LocalDate publicationDate,
        @NotBlank
        String studentId) {

}
