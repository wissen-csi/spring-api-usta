package com.usta.edu.co.MedicineRotationManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record Err( 
    @Positive
    int code, 
    @NotBlank
    String message,  
    @NotBlank
    String status
) {

}
