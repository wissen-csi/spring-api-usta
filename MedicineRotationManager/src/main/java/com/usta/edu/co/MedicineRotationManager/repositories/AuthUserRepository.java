package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.*;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

     Optional<AuthUser> findByDni(String dni);

     boolean existsByDni(String dni);

     List<AuthUser> findByEnabledTrue();

     List<AuthUser> findByRole(AppRole role);

     AuthUser save(AuthUser repository);

}
