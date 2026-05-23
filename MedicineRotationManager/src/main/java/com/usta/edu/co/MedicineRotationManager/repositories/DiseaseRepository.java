package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Disease;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease,String> {

    Optional<Disease> findByCode(String code);
}
