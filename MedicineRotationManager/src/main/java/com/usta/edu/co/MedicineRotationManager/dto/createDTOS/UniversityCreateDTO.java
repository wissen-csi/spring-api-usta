package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import jakarta.validation.constraints.*;

public record UniversityCreateDTO(
        @NotNull
        LocationCreateDTO address,
        @Email
        String email,
        @NotBlank
        String name,
        @NotBlank
        @Pattern(regexp = "^\\d+$")
        String phoneNumber) {

}
