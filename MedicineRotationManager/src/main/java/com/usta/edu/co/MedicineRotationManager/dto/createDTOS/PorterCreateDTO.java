package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

public record PorterCreateDTO(
        String name,
        String lastName,
        String dni,
        MaritalStatus maritalStatus,
        String placeBirthId,
        String residenceAddressId,
        String phoneNumber,
        String email,
        TypeBlood typeBlood,
        double weight,
        double imc,
        LocalDate hireDate,
        String employeeCode,
        boolean isActive

) {

}