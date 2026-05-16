package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.RotationCreationDTO;
import com.usta.edu.co.MedicineRotationManager.models.Doctor;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.repositories.RotationRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceRotation {

    private final RotationRepository repository;
    private final ServiceDoctor serviceDoctor;
    private final ObjectMapper objectMapper;

    public ServiceRotation(RotationRepository repository,
            ServiceDoctor serviceDoctor,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.serviceDoctor = serviceDoctor;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void save(RotationCreationDTO dto) {

        Doctor doctor = serviceDoctor.findById(dto.doctorId());

        if (dto.startDate().isAfter(dto.completionDate())) {
            throw new IllegalArgumentException("Start date cannot be after completion date");
        }

        Rotation rotation = Rotation.builder()
                .id(UUIDGenerator.generateNewId())
                .doctor(doctor)
                .hospitalLocation(dto.hospitalLocation())
                .typeRotation(dto.typeRotation())
                .startDate(dto.startDate())
                .completionDate(dto.completionDate())
                .capacity(dto.capacity())
                .build();

        repository.save(rotation);
    }

    public Rotation findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rotation not found with id: " + id));
    }

    public Page<Rotation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public void delete(String id) {
        Rotation rotation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rotation not found with id: " + id));

        repository.delete(rotation);
    }

    @Transactional
    public void patch(String id, RotationCreationDTO dto) {

        Rotation rotation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rotation not found with id: " + id));

        Doctor doctor = serviceDoctor.findById(dto.doctorId());

        rotation.setDoctor(doctor);
        rotation.setHospitalLocation(dto.hospitalLocation());
        rotation.setTypeRotation(dto.typeRotation());
        rotation.setStartDate(dto.startDate());
        rotation.setCompletionDate(dto.completionDate());
        rotation.setCapacity(dto.capacity());

        repository.save(rotation);
    }

    @Transactional
    public void patch(String id, JsonNode node) {
        Rotation rotation = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(rotation).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(rotation);

    }

}
