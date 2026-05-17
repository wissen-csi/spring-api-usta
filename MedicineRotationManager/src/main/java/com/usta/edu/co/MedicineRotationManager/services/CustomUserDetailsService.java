package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.usta.edu.co.MedicineRotationManager.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  AuthUserRepository authUserRepository;

    /* Devuelve AuthUser (lo busca por DNI):) */
    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        return this.authUserRepository.findByDni(dni)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
