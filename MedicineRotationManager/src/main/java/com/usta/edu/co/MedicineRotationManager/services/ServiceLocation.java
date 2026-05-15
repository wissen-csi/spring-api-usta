package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.dto.LocationDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.repositories.LocationRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceLocation {

    private final LocationRepository repository;

    public ServiceLocation(LocationRepository repository) {
        this.repository = repository;
    }

    public Location findById(String id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

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