package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;

public record LocationCreateDTO(
    @NotBlank
    String address, 
    @NotBlank
    String city,
    @NotBlank 
    String department) {

}
