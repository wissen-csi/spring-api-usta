package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.TaskCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Admin;
import com.usta.edu.co.MedicineRotationManager.models.Task;
import com.usta.edu.co.MedicineRotationManager.repositories.TaskRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final ServiceAdmin adminService;

    public TaskService(TaskRepository repository, ServiceAdmin adminService) {
        this.repository = repository;
        this.adminService = adminService;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    @Transactional
    public void save(TaskCreateDTO dto) {
        Admin admin = adminService.findById(dto.adminId());
        repository.save(new Task(UUIDGenerator.generateNewId(), dto.description(), admin));
    }

    @Transactional
    public void delete(String id) {
        Task task = findById(id);
        repository.delete(task);
    }
}
