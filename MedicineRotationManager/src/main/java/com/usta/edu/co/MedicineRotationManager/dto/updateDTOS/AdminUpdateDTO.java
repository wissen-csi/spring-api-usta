package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminUpdateDTO(
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotNull
        MaritalStatus maritalStatus,
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
        double imc) {

}
