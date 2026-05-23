package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Attendant;
import com.usta.edu.co.MedicineRotationManager.models.Student;

public interface AttendantRepository extends JpaRepository<Attendant, String> {
    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, String id);

    List<Attendant> findByStudent(Student student);

    Optional<Attendant> findByDni(String dni);

    Page<Attendant>findAll(Pageable pageable);
}
