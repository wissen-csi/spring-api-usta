package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usta.edu.co.MedicineRotationManager.models.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query(value = """
            SELECT *
            FROM students
            WHERE DATEDIFF(arl_end_date,CURDATE())<=5
            """, nativeQuery = true)
    public List<Student> findByCloseToExpireArl();

    Optional<Student> findById(String id);

    @Query("SELECT s FROM Student s WHERE s.dni = :dni")
    Optional<Student> findByDni(@Param("dni") String dni);

}
