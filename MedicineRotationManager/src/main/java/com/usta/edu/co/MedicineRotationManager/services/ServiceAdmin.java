package com.usta.edu.co.MedicineRotationManager.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usta.edu.co.MedicineRotationManager.dto.AdminCreateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.models.Admin;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.repositories.AdminRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

public class ServiceAdmin {
    private AdminRepository adminRepository;
    private ServiceLocation serviceLocation;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public ServiceAdmin(AdminRepository adminRepository, ServiceLocation serviceLocation) {
        this.adminRepository = adminRepository;
        this.serviceLocation = serviceLocation;
    }
    public List<Admin> findAll(){
        return adminRepository.findAll();
    }
    public Admin findById(String id){
        return adminRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Admin with "+id+" no found"));
    }
    public void save(AdminCreateDTO dto){
        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
        adminRepository.save(Admin.builder()
        .id(UUIDGenerator.newId())
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
    .build()
);
    }

    
}
