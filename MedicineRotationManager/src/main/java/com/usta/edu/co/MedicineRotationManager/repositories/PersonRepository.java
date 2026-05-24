package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import com.usta.edu.co.MedicineRotationManager.models.Person;

public interface PersonRepository  extends JpaRepository<Person,String> {
    Optional<Person> findByDni(String dni);
@Query(
    value = """
        SELECT p.email
        FROM person p
        INNER JOIN auth_user au 
            ON p.auth_user_id = au.id
        WHERE au.role = :role
        """,
    nativeQuery = true
)
List<String> findEmailsByRole(@Param("role") String role);

}
