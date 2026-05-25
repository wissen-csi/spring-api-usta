package com.usta.edu.co.MedicineRotationManager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EntryPracticeCreationDTO(
    @NotBlank
    String title,
    @NotNull
    LocalDateTime startTime, 
    @NotNull
    LocalDateTime endTime,
    @NotBlank
    String idGroup) {
    
}
