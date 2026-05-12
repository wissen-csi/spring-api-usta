package com.usta.edu.co.MedicineRotationManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;

@Configuration
public class ConfigSecurity {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/test/**").hasAuthority(AppRole.ROLE_DOCTOR.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/test/**").hasAuthority(AppRole.ROLE_ADMIN.name())

                );
        return httpSecurity.build();
    }

}
