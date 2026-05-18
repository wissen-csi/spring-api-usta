package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Medicine;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine,String> {
   Page<Medicine>findAll(Pageable pageable);
}
