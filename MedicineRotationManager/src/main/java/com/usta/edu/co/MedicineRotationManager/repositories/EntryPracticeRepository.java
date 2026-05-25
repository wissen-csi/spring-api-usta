package com.usta.edu.co.MedicineRotationManager.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.models.Group;

public interface EntryPracticeRepository extends JpaRepository<EntryPractice,String> {
    @Query(value = """
        SELECT COUNT(ep) > 0
        FROM EntryPractice ep
        WHERE ep.group = :group
        AND ep.startTime < :end
        AND ep.endTime > :start
            """)
    public boolean existsScheduleConflict(Group group,LocalDateTime start,LocalDateTime end);

    Optional<EntryPractice> findByQrCode(String qrCode);

    @Query(value = """
        SELECT ep FROM EntryPractice ep
        JOIN ep.group g
        JOIN g.rotation r
        WHERE r.doctor.id = :doctorId
            """)
    Page<EntryPractice> findByDoctorId(String doctorId, Pageable pageable);
}
