package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Porter;

public interface PorterRepository extends JpaRepository<Porter, String> {
    Page<Porter> findAll(Pageable pageable);
}
