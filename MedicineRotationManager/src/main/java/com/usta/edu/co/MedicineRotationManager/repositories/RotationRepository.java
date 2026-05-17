package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Rotation;

public interface RotationRepository extends JpaRepository<Rotation,String> {
}
