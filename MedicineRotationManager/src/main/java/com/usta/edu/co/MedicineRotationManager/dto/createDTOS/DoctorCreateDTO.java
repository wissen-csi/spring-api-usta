package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.*;

public record DoctorCreateDTO(
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        @Pattern(regexp = "^\\d+$", message = "El DNI solo debe contener números")
        String dni,
        @NotNull
        MaritalStatus maritalStatus,
        @NotNull
        LocationCreateDTO placeBirth,
        @NotNull
        LocationCreateDTO residenceAddress,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email,
        @NotNull
        TypeBlood typeBlood,
        @Positive
        double weight,
        @Positive
        double imc,
        @NotBlank
        String password,
        @NotNull
        Specialty specialty,
        @NotBlank
        String universityId) {

}
