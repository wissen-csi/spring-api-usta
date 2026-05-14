package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usta.edu.co.MedicineRotationManager.models.*;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

    Optional<AuthUser> findByDni(String dni);
}
