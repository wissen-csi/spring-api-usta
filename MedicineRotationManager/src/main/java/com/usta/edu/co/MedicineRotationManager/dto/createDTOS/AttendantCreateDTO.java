package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.TypeAttendant;

import jakarta.validation.constraints.*;

public record AttendantCreateDTO(
    @NotBlank
    String name, 
    @NotBlank
    String lastName, 
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "El DNI solo debe contener números")
    String phoneNumber, 
    @NotNull
    TypeAttendant typeAttendant,
    @NotBlank
    String dni, 
    @NotNull
    String studentId) {

}
