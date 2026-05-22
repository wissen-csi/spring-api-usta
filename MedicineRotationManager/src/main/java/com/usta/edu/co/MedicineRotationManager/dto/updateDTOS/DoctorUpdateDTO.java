package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;


public record DoctorUpdateDTO(

        String name,
        String lastName,
        String dni,

        MaritalStatus maritalStatus,

        String phoneNumber,
        String email,

        TypeBlood typeBlood,
        double weight,
        double imc,

        Specialty specialty,

        String universityId

) {}