package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.repositories.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        return authUserRepository

                .findByUserName(username)

                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"));
    }
}