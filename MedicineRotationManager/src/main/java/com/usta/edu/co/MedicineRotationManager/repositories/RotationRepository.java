package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface RotationRepository extends JpaRepository<Rotation,String> {
    public Page<Rotation> findByDoctorId(String doctorId, Pageable pageable);

}
