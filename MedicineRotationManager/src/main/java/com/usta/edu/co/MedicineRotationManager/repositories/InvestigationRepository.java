package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Investigation;

public interface InvestigationRepository extends JpaRepository<Investigation, String> {

    Page<Investigation> findAll(Pageable pageable);
}
