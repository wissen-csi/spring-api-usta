package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record DiseaseCieDTO( 
    @NotBlank
    String fundationURI, 
    @NotBlank
    String code, 
    @NotBlank 
    String label) {

}
