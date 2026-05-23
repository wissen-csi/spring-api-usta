package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

public record UniversityCreateDTO(
        LocationCreateDTO address,
        String email,
        String name,
        String phoneNumber) {

}
