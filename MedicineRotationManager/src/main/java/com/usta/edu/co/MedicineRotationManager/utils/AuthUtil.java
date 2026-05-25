package com.usta.edu.co.MedicineRotationManager.utils;

import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static String getCurrentUserDni() {
        AuthUser authUser = (AuthUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return authUser.getUsername();
    }
}
