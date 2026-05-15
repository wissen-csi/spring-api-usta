package com.usta.edu.co.MedicineRotationManager.dto;
import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

public record AdminCreateDTO(
    String name,
    String lastName,
    String dni,
    MaritalStatus maritalStatus,
    LocationDTO placeBirth,
    LocationDTO residenceAddress,
    String phoneNumber,
    String email,
    TypeBlood typeBlood,
    double weight,
    double imc,
    String password,
    LocalDate hiringDate,
    LocalDate endDate
) {}
