package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
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
        Double weight,
        Double imc,
        Specialty specialty,
        String universityId,
        LocationCreateDTO placeBirth,
        LocationCreateDTO residenceAddress

) {}
