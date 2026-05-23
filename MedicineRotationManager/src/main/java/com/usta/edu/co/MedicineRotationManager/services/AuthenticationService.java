package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.dto.LoginRequestDTO;
import com.usta.edu.co.MedicineRotationManager.dto.LoginResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final AuthUserRepository authUserRepository;

    public LoginResponseDTO authenticate(
            LoginRequestDTO request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getDni(),

                        request.getPassword()));

        AuthUser user = authUserRepository

                .findByUserName(request.getDni())

                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        String token =
                jwtService.generateToken(user);

        return new LoginResponseDTO(token);
    }
}