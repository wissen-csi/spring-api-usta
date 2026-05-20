package com.usta.edu.co.MedicineRotationManager.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usta.edu.co.MedicineRotationManager.models.GroupAssignment;
import com.usta.edu.co.MedicineRotationManager.models.Student;

public interface GroupAssignmentRepository extends JpaRepository<GroupAssignment,String> {
    public long countByGroupId(String id);
    @Query("""
        SELECT COUNT(ga) > 0
        FROM GroupAssignment ga
        WHERE ga.student = :student
        AND ga.group.rotation.startDate <= :endDate
        AND ga.group.rotation.completionDate >= :startDate
    """)
    public boolean existsScheduleConflict(Student student,LocalDate startDate,LocalDate endDate);

}
