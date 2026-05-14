package com.usta.edu.co.MedicineRotationManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usta.edu.co.MedicineRotationManager.models.Task;

public interface TaskRepository extends JpaRepository<Task,String> {

}
