package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Location;

public interface LocationRepository extends JpaRepository<Location,String> {
    public Optional<Location> findByCityAndDepartmentAndAddress(
            String city,
            String department,
            String address
    );
}
