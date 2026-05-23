package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;
import lombok.Builder;
@Builder
public record AdminResponseDTO(String id,
        String name,

        String lastName,

        String dni,

        String email,

        String phoneNumber,

        MaritalStatus maritalStatus,

        TypeBlood typeBlood,

        double weight,

        double imc,

        LocalDate hiringDate,

        LocalDate endDate
) {

}
