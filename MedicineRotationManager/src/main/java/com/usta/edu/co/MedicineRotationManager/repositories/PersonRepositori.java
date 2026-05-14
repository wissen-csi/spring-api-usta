package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.Person;

public interface PersonRepositori  extends JpaRepository<Person,String> {
    @Query(value = """
            SELECT s.email
            FROM Person s
            WHERE s.role = :role

            """)
    public List<String> findEmailsByRole(AppRole role);
}
