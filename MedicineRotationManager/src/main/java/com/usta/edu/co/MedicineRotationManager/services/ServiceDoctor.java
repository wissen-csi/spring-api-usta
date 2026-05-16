package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DoctorCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Doctor;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.DoctorRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceDoctor {
    private DoctorRepository repository;
    private ObjectMapper objectMapper;
    private ServiceLocation serviceLocation;
    private PasswordEncoder passwordEncoder;
    private ServiceUniversity serviceUniversity;

    public ServiceDoctor(DoctorRepository repository, ObjectMapper objectMapper, ServiceLocation serviceLocation,
            PasswordEncoder passwordEncoder, ServiceUniversity serviceUniversity) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.serviceLocation = serviceLocation;
        this.passwordEncoder = passwordEncoder;
        this.serviceUniversity = serviceUniversity;
    }

    public Doctor findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public void delete(String id) {
        Doctor doctor = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        repository.delete(doctor);
    }

    @Transactional
    public void patch(String id, JsonNode node) {
        Doctor doctor = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(doctor).readValue(node);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        repository.save(doctor);
    }

    @Transactional
    public void save(DoctorCreateDTO dto) {
        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
        University university = serviceUniversity.findById(dto.universityId());
        repository.save(Doctor.builder()
                .id(UUIDGenerator.generateNewId())
                .name(dto.name())
                .lastName(dto.lastName())
                .dni(dto.dni())
                .maritalStatus(dto.maritalStatus())
                .placeBirth(placeBirth)
                .residenceAddress(residenceAddress)
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .typeBlood(dto.typeBlood())
                .weight(dto.weight())
                .imc(dto.imc())
                .password(passwordEncoder.encode(dto.password()))
                .specialty(dto.specialty())
                .university(university)
                .build());

    }
}
