package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.GroupCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Group;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.repositories.GroupRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

public class ServiceGroup {
    private ObjectMapper objectMapper;
    private GroupRepository repository;
    private ServiceRotation serviceRotation;
    public ServiceGroup(ObjectMapper objectMapper, GroupRepository repository, ServiceRotation serviceRotation) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.serviceRotation = serviceRotation;
    }
    public Group findById(String id){
        return repository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }
    public Page<Group> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
    @Transactional
    public void delete(String id){
        Group group = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        repository.delete(group);
    }
    @Transactional
    public void save(GroupCreateDTO dto){
        Rotation rotation = serviceRotation.findById(dto.rotationId());
        repository.save(Group.builder()
        .id(UUIDGenerator.generateNewId())
        .rotation(rotation)
        .capacity(dto.capacity())
        .build()
    );
    }

    @Transactional
    public void patch(String id, JsonNode node){
        Group group = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(group).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(group);
    }    
}
