package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;

public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, String> {

    public boolean existsByName(String name);

    public boolean existsByNameAndIdNot(String name, String id);

    public Page<AcademicPeriod> findAll(Pageable pageable);
}