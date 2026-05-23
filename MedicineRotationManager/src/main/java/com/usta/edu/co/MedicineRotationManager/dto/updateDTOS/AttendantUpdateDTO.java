package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.TypeAttendant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttendantUpdateDTO(
    @NotBlank
    String name, 
    @NotBlank
    String lastName, 
    @NotBlank
    String phoneNumber, 
    @NotBlank
    String dni,
    @NotNull
    TypeAttendant typeAttendant) {

}
