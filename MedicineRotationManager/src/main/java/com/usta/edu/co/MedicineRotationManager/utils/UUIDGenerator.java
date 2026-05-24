package com.usta.edu.co.MedicineRotationManager.utils;

import java.util.UUID;

public class UUIDGenerator {

    public static String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public static String encryptUUID() {
        return UUID.randomUUID().toString();
    }
}
