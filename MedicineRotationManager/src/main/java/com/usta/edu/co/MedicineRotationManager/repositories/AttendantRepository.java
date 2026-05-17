package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Attendant;

public interface AttendantRepository extends JpaRepository<Attendant, String> {

}
