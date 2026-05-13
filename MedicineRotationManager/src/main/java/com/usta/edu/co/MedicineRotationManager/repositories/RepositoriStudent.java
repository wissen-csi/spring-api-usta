package com.usta.edu.co.MedicineRotationManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usta.edu.co.MedicineRotationManager.models.Student;

public interface RepositoriStudent extends JpaRepository<Student,String> {
    @Query(value = """
            SELECT *
            FROM students
            WHERE DATEDIFF(arl_end_date, NOW())
                  BETWEEN 0 AND 5
            """,
            nativeQuery = true
    )
   public List<Student> findByCloseToExpireArl();
}
