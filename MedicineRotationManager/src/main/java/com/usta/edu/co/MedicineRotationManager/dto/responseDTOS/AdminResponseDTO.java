package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class AdminResponseDTO {
    private final String id;
    private final String name;
    private final String lastName;
    private final String dni;
    private final String email;
    private final String phoneNumber;
    private final MaritalStatus maritalStatus;
    private final TypeBlood typeBlood;
    private final double weight;
    private final double imc;
    private final LocalDate hiringDate;
    private final LocalDate endDate;

}
