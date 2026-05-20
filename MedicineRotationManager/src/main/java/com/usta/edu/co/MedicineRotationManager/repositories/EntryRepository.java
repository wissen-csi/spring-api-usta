package com.usta.edu.co.MedicineRotationManager.repositories;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/features-crud

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.*;

public interface EntryRepository extends JpaRepository<Entry, String> {
    Page<Entry> findAll(Pageable pageable);
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Entry;

public interface EntryRepository extends JpaRepository<Entry,String> {
>>>>>>> origin/features-crud
=======
>>>>>>> origin/features-crud

}
