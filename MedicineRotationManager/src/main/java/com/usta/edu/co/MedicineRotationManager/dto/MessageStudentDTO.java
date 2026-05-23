package com.usta.edu.co.MedicineRotationManager.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageStudentDTO(
    @NotBlank
    String name, 
    @NotBlank
    String dni, 
    @NotNull
    LocalDate endDate, 
    @NotNull
    Boolean status) {

}
