package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record DoctorUpdateDTO(
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        String dni,
        @NotNull
        MaritalStatus maritalStatus,
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
        @NotNull
        Specialty specialty,
        @NotBlank
        String universityId

) {}