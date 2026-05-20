package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.MedicalTreatment;

import java.awt.print.Pageable;

public interface MedicalTreatmentRepository extends JpaRepository<MedicalTreatment,String> {


}
