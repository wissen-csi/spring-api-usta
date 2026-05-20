package com.usta.edu.co.MedicineRotationManager.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.models.Group;

public interface EntryPracticeRepository extends JpaRepository<EntryPractice,String> {
    @Query(value = """
        SELECT COUNT(ep) > 0
        FROM EntryPractice ep
        WHERE ep.group = :group
        AND ep.endTime <= :end
        AND ep.startTime >= :start
            """)
    public boolean existsScheduleConflict(Group group,LocalDateTime start,LocalDateTime end);
}
