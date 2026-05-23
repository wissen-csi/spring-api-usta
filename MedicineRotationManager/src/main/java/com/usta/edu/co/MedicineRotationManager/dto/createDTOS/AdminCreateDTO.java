package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record AdminCreateDTO(

    @NotBlank
    String name,

    @NotBlank
    String lastName,

    @NotBlank
    @Pattern(regexp = "^\\d+$")
    String dni,

    @NotNull
    MaritalStatus maritalStatus,

    @NotNull
    @Valid
    LocationCreateDTO placeBirth,

    @NotNull
    @Valid
    LocationCreateDTO residenceAddress,

    @NotBlank
    @Pattern(regexp = "^\\d+$")
    String phoneNumber,

    @NotBlank
    @Email
    String email,

    @NotNull
    TypeBlood typeBlood,

    @Positive
    double weight,

    @Positive
    double imc,

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    String password,

    @NotNull
    LocalDate hiringDate,

    @NotNull
    LocalDate endDate

) {}