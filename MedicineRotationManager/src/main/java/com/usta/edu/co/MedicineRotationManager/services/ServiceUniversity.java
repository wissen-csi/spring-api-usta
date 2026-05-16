package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.UniversityCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.UniversityRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
@Service
public class ServiceUniversity {
    private UniversityRepository repository;
    private ServiceLocation serviceLocation;
    private ObjectMapper objectMapper;
    public ServiceUniversity(UniversityRepository repository, ServiceLocation serviceLocation, ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.serviceLocation = serviceLocation;
        this.objectMapper=objectMapper;
    }
    public void save(UniversityCreateDTO dto){
        Location location = serviceLocation.findOrCreate(dto.address());
        repository.save(University.builder()
    .id(UUIDGenerator.generateNewId())
    .address(location)
    .name(dto.name())
    .phoneNumber(dto.phoneNumberm())
    .isActive(true)
    .build()
    );
    }
    public void Delete(String id){
        University university = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        repository.delete(university);
    }
    public void softDelete(String id){
        University university =repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        university.setActive(false);
    }
    public void restore(String id){
        University university =repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        university.setActive(true);
    }
    public void patch(String id, JsonNode node){
        University university = repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(university).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(university);
    }
    public List<University> findAll(){
        return repository.findAll();
    }
    public University findById(String id){
        return repository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }
}
