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

        if (!dto.startTime().isBefore(dto.endTime())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        if (!rotation.getStartDate().isBefore(dto.startTime().toLocalDate())) {
            throw new IllegalArgumentException("La práctica debe iniciar después de la fecha de inicio de la rotación");
        }
        if (!rotation.getCompletionDate().isAfter(dto.endTime().toLocalDate())) {
            throw new IllegalArgumentException("La práctica debe terminar antes de la fecha de fin de la rotación");
        }
        if (repository.existsScheduleConflict(group, dto.startTime(), dto.endTime())) {
            throw new IllegalArgumentException("Ya existe una práctica en ese horario para este grupo");
        }

        repository.save(EntryPractice.builder()
                .title(dto.title())
                .endTime(dto.endTime())
                .startTime(dto.startTime())
                .group(group)
                .qrCode(UUIDGenerator.encryptUUID())
                .id(UUIDGenerator.generateNewId())
                .build());
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

    public EntryPractice findByQrCode(String qrCode) {
        return repository.findByQrCode(qrCode)
                .orElseThrow(() -> new EntityNotFoundException("Práctica no encontrada para este código QR"));
    }

    public Page<EntryPractice> findByDoctorId(String doctorId, Pageable pageable) {
        return repository.findByDoctorId(doctorId, pageable);
    }

    public void update(String id, EntryPracticeCreationDTO dto) {
        EntryPractice entryPractice = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Group group = serviceGroup.findById(dto.idGroup());
        Rotation rotation = group.getRotation();

        if (!dto.startTime().isBefore(dto.endTime())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        if (!rotation.getStartDate().isBefore(dto.startTime().toLocalDate())) {
            throw new IllegalArgumentException("La práctica debe iniciar después de la fecha de inicio de la rotación");
        }
        if (!rotation.getCompletionDate().isAfter(dto.endTime().toLocalDate())) {
            throw new IllegalArgumentException("La práctica debe terminar antes de la fecha de fin de la rotación");
        }

        entryPractice.setTitle(dto.title());
        entryPractice.setStartTime(dto.startTime());
        entryPractice.setEndTime(dto.endTime());
        entryPractice.setGroup(group);
        repository.save(entryPractice);
    }

}
