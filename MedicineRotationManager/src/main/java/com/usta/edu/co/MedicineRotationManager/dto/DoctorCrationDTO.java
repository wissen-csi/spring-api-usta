package com.usta.edu.co.MedicineRotationManager.dto;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

public record DoctorCrationDTO(
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
    Specialty specialty,
    String universityId
) {

}
