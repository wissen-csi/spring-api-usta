package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.MedicationTreatment;

public interface MedicationTreatmentRepository extends JpaRepository<MedicationTreatment,String> {

}
