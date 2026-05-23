package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.EntryPracticeCreationDTO;
import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.models.Group;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.repositories.EntryPracticeRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
@Service
public class ServiceEntryPractice {
    private EntryPracticeRepository repository;
    private ServiceGroup serviceGroup;

    public ServiceEntryPractice(EntryPracticeRepository repository, ServiceGroup serviceGroup) {
        this.repository = repository;
        this.serviceGroup = serviceGroup;
    }

    @Transactional
    public void save(EntryPracticeCreationDTO dto) {
        Group group = serviceGroup.findById(dto.idGroup());
        Rotation rotation = group.getRotation();
        if (dto.starTime().isBefore(dto.endTime()) && rotation.getStartDate().isBefore(dto.starTime().toLocalDate())
                && rotation.getCompletionDate().isAfter(dto.endTime().toLocalDate())&& !repository.existsScheduleConflict(group, dto.starTime(), dto.endTime())) {
            repository.save(EntryPractice.builder()
                    .endTime(dto.endTime())
                    .startTime(dto.starTime())
                    .group(group)
                    .qrCode(UUIDGenerator.encryptUUID())
                    .id(UUIDGenerator.generateNewId())
                    .build());
        }
    }

    @Transactional
    public void delete(String id) {
        EntryPractice entryPractice = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        repository.delete(entryPractice);
    }

    public EntryPractice findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public Page<EntryPractice> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void update(String id, EntryPracticeCreationDTO dto) {
        EntryPractice entryPractice = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Group group = serviceGroup.findById(dto.idGroup());
        Rotation rotation = group.getRotation();
        if (dto.starTime().isBefore(dto.endTime()) && rotation.getStartDate().isBefore(dto.starTime().toLocalDate())
                && rotation.getCompletionDate().isAfter(dto.endTime().toLocalDate())) {
            entryPractice.setEndTime(dto.endTime());
            entryPractice.setEndTime(dto.endTime());
            entryPractice.setGroup(group);
            repository.save(entryPractice);
        }

    }

}
