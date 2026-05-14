package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.usta.edu.co.MedicineRotationManager.dto.LoginRequestDTO;
import com.usta.edu.co.MedicineRotationManager.dto.LoginResponseDTO;
import com.usta.edu.co.MedicineRotationManager.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate")
    public String validateStudentAccess() {
        return "Access validated successfully";
    }

}
