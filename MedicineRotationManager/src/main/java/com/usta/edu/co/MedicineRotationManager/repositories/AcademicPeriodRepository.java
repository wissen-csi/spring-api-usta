package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;

public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, String> {
    boolean existsByName(String name);

    List<AcademicPeriod> findByIsActiveTrue();

    boolean existsByNameAndIdNot(String name, String id);

    Page<AcademicPeriod> findAll(Pageable pageable);
}
