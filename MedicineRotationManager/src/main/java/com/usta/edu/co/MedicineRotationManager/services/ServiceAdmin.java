package com.usta.edu.co.MedicineRotationManager.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AdminCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AdminUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.Admin;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.repositories.AdminRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceAdmin {
    private AdminRepository adminRepository;
    private ServiceLocation serviceLocation;
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ServiceAdmin(AdminRepository adminRepository, ServiceLocation serviceLocation, ObjectMapper objectMapper) {
        this.adminRepository = adminRepository;
        this.serviceLocation = serviceLocation;
        this.objectMapper = objectMapper;
    }

    public Page<Admin> findAll(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public Admin findById(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin with " + id + " no found"));
    }

    @Transactional
    public void delete(String id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        adminRepository.delete(admin);
    }

    @Transactional
    public void update(String id, AdminUpdateDTO dto) {
        Location location = serviceLocation.findOrCreate(dto.residenceAddress());
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        admin.setName(dto.name());
        admin.setLastName(dto.lastName());
        admin.setMaritalStatus(dto.maritalStatus());
        admin.setResidenceAddress(location);
        admin.setPhoneNumber(dto.phoneNumber());
        admin.setEmail(dto.email());
        admin.setTypeBlood(dto.typeBlood());
        admin.setWeight(dto.weight());
        admin.setImc(dto.imc());
        adminRepository.save(admin);
    }

    @Transactional
    public void patch(String id, JsonNode node) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        try {
            objectMapper.readerForUpdating(admin).readValue(node);

        } catch (Exception e) {
            throw new RuntimeException();
        }
        adminRepository.save(admin);
    }

    @Transactional
    public void save(AdminCreateDTO dto) {
        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
        Admin admin = Admin.builder()
                .id(UUIDGenerator.generateNewId())
                .name(dto.name())
                .lastName(dto.lastName())
                .dni(dto.dni())
                .maritalStatus(dto.maritalStatus())
                .residenceAddress(residenceAddress)
                .placeBirth(placeBirth)
                .password(passwordEncoder.encode(dto.password()))
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .typeBlood(dto.typeBlood())
                .weight(dto.weight())
                .imc(dto.imc())
                .role(AppRole.ADMIN)
                .hiringDate(dto.hiringDate())
                .endDate(dto.endDate())
                .build();
        adminRepository.save(admin);

    }

}
