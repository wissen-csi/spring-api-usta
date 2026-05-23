package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Person;

@Repository
public interface AuthUserRepository
        extends JpaRepository<AuthUser, String> {

    Optional<AuthUser> findByUserName(
            String userName);

    boolean existsByUserName(
            String userName);

    List<AuthUser> findByEnabledTrue();

    List<AuthUser> findByRole(
            AppRole role);

    Optional<AuthUser> findByPerson(
            Person person);
}