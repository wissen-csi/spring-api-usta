package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record InvestigationUpdateDTO(
    @NotBlank
    String repositoryUrl, 
    @NotBlank
    String description, 
    @NotBlank
    LocalDate publicationLocalDate) {


}
