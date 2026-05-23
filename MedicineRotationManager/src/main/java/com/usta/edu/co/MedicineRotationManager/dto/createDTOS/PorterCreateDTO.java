package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.*;

public record PorterCreateDTO(
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        String dni,
        @NotNull
        MaritalStatus maritalStatus,
        @NotBlank
        String placeBirthId,
        @NotBlank
        String residenceAddressId,
        @NotBlank
        String phoneNumber,
        @Email
        String email,
        @NotNull
        TypeBlood typeBlood,
        @NotNull
        double weight,
        @Positive
        double imc,
        @NotNull
        LocalDate hireDate,
        @NotNull
        String employeeCode,
        @NotNull
        boolean isActive

) {

}