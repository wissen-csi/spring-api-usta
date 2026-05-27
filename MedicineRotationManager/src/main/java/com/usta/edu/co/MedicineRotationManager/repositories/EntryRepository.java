package com.usta.edu.co.MedicineRotationManager.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.*;

public interface EntryRepository extends JpaRepository<Entry, String> {
    Page<Entry> findAll(Pageable pageable);
    boolean existsByStudent_IdAndEntryPractice_Id(String studentId,String entryPracticeId);

    List<Entry> findByEntryPracticeId(String entryPracticeId);

}
