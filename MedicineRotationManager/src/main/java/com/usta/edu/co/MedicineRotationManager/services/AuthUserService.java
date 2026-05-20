package com.usta.edu.co.MedicineRotationManager.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.Admin;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Doctor;
import com.usta.edu.co.MedicineRotationManager.models.Person;
import com.usta.edu.co.MedicineRotationManager.models.Porter;
import com.usta.edu.co.MedicineRotationManager.models.Student;

import com.usta.edu.co.MedicineRotationManager.repositories.AuthUserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(
            Person person,
            String rawPassword) {

        AppRole role = getRole(person);

        AuthUser authUser = AuthUser.builder()
                .person(person)
                .userName(person.getDni())
                .password(
                        passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        repository.save(authUser);
    }

    @Transactional
    public void updateUsername(
            Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setUserName(person.getDni());

        repository.save(authUser);
    }

    @Transactional
    public void updatePassword(
            Person person,
            String newPassword) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setPassword(
                passwordEncoder.encode(newPassword));

        authUser.setPasswordChangeDate(
                LocalDateTime.now());

        repository.save(authUser);
    }

    @Transactional
    public void registerSuccessfulLogin(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setLastLogin(LocalDateTime.now());

        authUser.setFailedAttempts(0);

        repository.save(authUser);
    }

    @Transactional
    public void registerFailedLogin(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        int attempts = authUser.getFailedAttempts() + 1;

        authUser.setFailedAttempts(attempts);

        if (attempts >= 5) {

            authUser.setAccountLocked(true);
        }

        repository.save(authUser);
    }

    @Transactional
    public void unlockUser(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setAccountLocked(false);

        authUser.setFailedAttempts(0);

        repository.save(authUser);
    }

    @Transactional
    public void disableUser(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setEnabled(false);

        repository.save(authUser);
    }

    @Transactional
    public void enableUser(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setEnabled(true);

        repository.save(authUser);
    }

    @Transactional
    public void saveRefreshToken(
            Person person,
            String refreshToken,
            LocalDateTime expiration) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setRefreshToken(refreshToken);

        authUser.setRefreshTokenExpiration(
                expiration);

        repository.save(authUser);
    }

    @Transactional
    public void removeRefreshToken(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        authUser.setRefreshToken(null);

        authUser.setRefreshTokenExpiration(null);

        repository.save(authUser);
    }

    @Transactional
    public void deleteUser(Person person) {

        AuthUser authUser = repository
                .findByPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Auth user not found"));

        repository.delete(authUser);
    }

    private AppRole getRole(Person person) {

        if (person instanceof Doctor) {

            return AppRole.DOCTOR;

        } else if (person instanceof Admin) {

            return AppRole.ADMIN;

        } else if (person instanceof Student) {

            return AppRole.STUDENT;

        } else if (person instanceof Porter) {

            return AppRole.PORTER;
        }

        throw new IllegalArgumentException(
                "Invalid person type");
    }
}