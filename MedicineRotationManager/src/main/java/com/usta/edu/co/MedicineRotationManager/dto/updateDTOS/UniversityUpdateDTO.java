package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UniversityUpdateDTO(
        @NotBlank
        String name,
        @Email
        String email,
        @Pattern(regexp = "^\\d+$")
        String phoneNumber,
        @NotNull
        Boolean isActive,
        @NotNull
        LocationCreateDTO address

) {
}