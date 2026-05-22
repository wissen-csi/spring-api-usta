package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;

import lombok.Builder;

@Builder
public record UniversityUpdateDTO(

        String name,
        String email,
        String phoneNumber,
        Boolean isActive,

        LocationCreateDTO address

) {
}