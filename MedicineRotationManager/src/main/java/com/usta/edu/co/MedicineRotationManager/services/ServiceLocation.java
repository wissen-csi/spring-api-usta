package com.usta.edu.co.MedicineRotationManager.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.repositories.LocationRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ServiceLocation {

    private LocationRepository repository;
    private ObjectMapper objectMapper;

    public ServiceLocation(LocationRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public Location findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public Page<Location> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Transactional
    public void update(String id, LocationCreateDTO dto) {
        Location location = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        location.setAddress(dto.address());
        location.setDepartment(dto.department());
        location.setCity(dto.city());
    }

    @Transactional
    public void save(LocationCreateDTO dto) {
        repository.save(new Location(UUIDGenerator.generateNewId(), dto.address(), dto.city(), dto.department()));

    }

    @Transactional
    public void patch(String id, JsonNode node) {
        Location location = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(location).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(location);
    }

    @Transactional
    public Location findOrCreate(LocationCreateDTO dto) {
        return repository
                .findByCityAndDepartmentAndAddress(
                        dto.city(),
                        dto.department(),
                        dto.address())
                .orElseGet(() -> repository.save(
                        new Location(
                                UUIDGenerator.generateNewId(),
                                dto.address(),
                                dto.city(),
                                dto.department())));
    }

    @Transactional
    public Location findOrCreate(String city, String department, String address) {
        return repository
                .findByCityAndDepartmentAndAddress(
                        city,
                        department,
                        address)
                .orElseGet(() -> repository.save(
                        new Location(
                                UUIDGenerator.generateNewId(),
                                address,
                                city,
                                department)));
    }
}