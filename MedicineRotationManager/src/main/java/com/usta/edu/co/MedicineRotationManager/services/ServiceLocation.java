package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.LocationDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.repositories.LocationRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ServiceLocation {

    private  LocationRepository repository;
    private ObjectMapper objectMapper;

    public ServiceLocation(LocationRepository repository,ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper=objectMapper;
    }

    public Location findById(String id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }
    public Page<Location> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
    @Transactional
    public void delete(String id){
        repository.deleteById(id);
    }
    @Transactional
    public void update(String id,LocationDTO dto){
        Location location = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        location.setAddress(dto.address());
        location.setDepartment(dto.department());
        location.setCity(dto.city());
    }
    @Transactional
    public void save(LocationDTO dto){
        repository.save(new Location(UUIDGenerator.newId(), dto.address(), dto.city(), dto.department()));

    }
    @Transactional
    public void patch(String id, JsonNode node){
        Location location = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(location).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(location);
    }
    @Transactional
    public Location findOrCreate(LocationDTO dto) {
        return repository
            .findByCityAndDepartmentAndAddress(
                dto.city(),
                dto.department(),
                dto.address()
            )
            .orElseGet(() -> repository.save(
                new Location(
                    UUIDGenerator.newId(),
                    dto.address(),
                    dto.city(),
                    dto.department()
                )
            ));
    }
}