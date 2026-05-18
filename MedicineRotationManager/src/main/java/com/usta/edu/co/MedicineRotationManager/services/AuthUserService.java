package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;


    public void addAuthUser(){

    }
}
