package com.usta.edu.co.MedicineRotationManager.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DoctorCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.DoctorUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Doctor;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.DoctorRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usta.edu.co.MedicineRotationManager.models.GroupAssignment;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.models.Group;
import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.repositories.GroupAssignmentRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.GroupRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.EntryPracticeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceDoctor {
    private DoctorRepository repository;
    private ObjectMapper objectMapper;
    private ServiceLocation serviceLocation;
    private ServiceUniversity serviceUniversity;
    private AuthUserService userService;
    private GroupAssignmentRepository groupAssignmentRepository;
    private EntryPracticeRepository entryPracticeRepository;
    private GroupRepository groupRepository;

    public ServiceDoctor(DoctorRepository repository, ObjectMapper objectMapper, ServiceLocation serviceLocation,
            ServiceUniversity serviceUniversity, AuthUserService userService,
            GroupAssignmentRepository groupAssignmentRepository,
            EntryPracticeRepository entryPracticeRepository,
            GroupRepository groupRepository) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.serviceLocation = serviceLocation;
        this.serviceUniversity = serviceUniversity;
        this.userService = userService;
        this.groupAssignmentRepository = groupAssignmentRepository;
        this.entryPracticeRepository = entryPracticeRepository;
        this.groupRepository = groupRepository;
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

        List<Rotation> rotations = doctor.getRotations();
        for (Rotation rotation : rotations) {
            List<Group> groups = rotation.getGroups();
            for (Group group : groups) {
                List<GroupAssignment> assignments = group.getGroupAssignments();
                for (GroupAssignment ga : assignments) {
                    groupAssignmentRepository.delete(ga);
                }
                List<EntryPractice> practices = group.getEntryPractices();
                for (EntryPractice ep : practices) {
                    entryPracticeRepository.delete(ep);
                }
                groupRepository.delete(group);
            }
        }

        userService.deleteUser(doctor);
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
        doctor=repository.save(doctor);
        userService.updateUsername(doctor);
    }

    @Transactional
    public void save(DoctorCreateDTO dto) {
        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
        University university = serviceUniversity.findById(dto.universityId());
        Doctor doctor = Doctor.builder()
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
                .specialty(dto.specialty())
                .university(university)
                .build();
        doctor = repository.save(doctor);
        userService.createUser(doctor, dto.password());
    }
    @Transactional
    public void update(String id, DoctorUpdateDTO dto) {

        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        if (dto.name() != null) doctor.setName(dto.name());
        if (dto.lastName() != null) doctor.setLastName(dto.lastName());
        if (dto.dni() != null) doctor.setDni(dto.dni());
        if (dto.maritalStatus() != null) doctor.setMaritalStatus(dto.maritalStatus());
        if (dto.phoneNumber() != null) doctor.setPhoneNumber(dto.phoneNumber());
        if (dto.email() != null) doctor.setEmail(dto.email());
        if (dto.typeBlood() != null) doctor.setTypeBlood(dto.typeBlood());
        if (dto.weight() != null) doctor.setWeight(dto.weight());
        if (dto.imc() != null) doctor.setImc(dto.imc());
        if (dto.specialty() != null) doctor.setSpecialty(dto.specialty());
        if (dto.universityId() != null) {
            University university = serviceUniversity.findById(dto.universityId());
            doctor.setUniversity(university);
        }
        if (dto.placeBirth() != null) {
            Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
            doctor.setPlaceBirth(placeBirth);
        }
        if (dto.residenceAddress() != null) {
            Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
            doctor.setResidenceAddress(residenceAddress);
        }
        repository.save(doctor);

        userService.updateUsername(doctor);
    }
}
