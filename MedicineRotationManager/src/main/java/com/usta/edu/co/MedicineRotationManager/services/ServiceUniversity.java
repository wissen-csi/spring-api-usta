package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.UniversityCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.UniversityUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.UniversityRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceUniversity {
    private UniversityRepository repository;
    private ServiceLocation serviceLocation;
    private ObjectMapper objectMapper;

    public ServiceUniversity(UniversityRepository repository, ServiceLocation serviceLocation,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.serviceLocation = serviceLocation;
        this.objectMapper = objectMapper;
    }

    public void save(UniversityCreateDTO dto) {
        Location location = serviceLocation.findOrCreate(dto.address());
        repository.save(University.builder()
                .id(UUIDGenerator.generateNewId())
                .address(location)
                .name(dto.name())
                .email(dto.email())
                .phoneNumber(dto.phoneNumberm())
                .isActive(true)
                .build());
    }

    public void delete(String id) {
        University university = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        repository.delete(university);
    }

    public void softDelete(String id) {
        University university = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        university.setActive(false);
    }

    public void restore(String id) {
        University university = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        university.setActive(true);
    }

    public void patch(String id, JsonNode node) {
        University university = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(university).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(university);
    }

    public Page<University> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public University findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public void update(
            String id,
            UniversityUpdateDTO dto) {

        University university = repository.findById(id)
                .orElseThrow(
                        EntityNotFoundException::new);

        university.setName(dto.name());

        university.setEmail(dto.email());

        university.setPhoneNumber(
                dto.phoneNumber());

        university.setActive(
                dto.isActive());

        Location location = serviceLocation.findOrCreate(
                dto.address());

        university.setAddress(location);

        repository.save(university);
    }
}
