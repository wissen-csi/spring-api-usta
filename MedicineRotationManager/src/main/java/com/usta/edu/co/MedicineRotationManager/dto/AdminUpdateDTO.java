package com.usta.edu.co.MedicineRotationManager.dto;


import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

public record AdminUpdateDTO(
    String name,
    String lastName,
    MaritalStatus maritalStatus,
    LocationDTO residenceAddress,
    String phoneNumber,
    String email,
    TypeBlood typeBlood,
    double weight,
    double imc
) {

}
