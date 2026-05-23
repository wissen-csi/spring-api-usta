package com.usta.edu.co.MedicineRotationManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank
    private String dni;
    @NotBlank
    private String password;

}
