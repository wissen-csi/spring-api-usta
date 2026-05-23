package com.usta.edu.co.MedicineRotationManager.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UUIDGenerator {
    @Autowired
    private static PasswordEncoder passwordEncoder;

    public static String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public static String encryptUUID() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }
}
