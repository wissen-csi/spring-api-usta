package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceStudent {

    private StudentRepository repository;

    private ObjectMapper objectMapper;

    private ServiceLocation serviceLocation;

    private PasswordEncoder passwordEncoder;

    private ServiceUniversity serviceUniversity;

    public ServiceStudent(StudentRepository repository, ObjectMapper objectMapper, ServiceLocation serviceLocation, PasswordEncoder passwordEncoder, ServiceUniversity serviceUniversity) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.serviceLocation = serviceLocation;
        this.passwordEncoder = passwordEncoder;
        this.serviceUniversity = serviceUniversity;
    }

    public Student findById(String id){
        return repository.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Student not found with id: " + id
                )
            );
    }

    public Page<Student> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public List<Student> findCloseToExpireARL(){
        return repository.findByCloseToExpireArl();
    }

    @Transactional
    public void save(StudentCreateDTO dto){

        Location placeBirth =
            serviceLocation.findOrCreate(dto.placeBirth());

        Location residenceAddress =
            serviceLocation.findOrCreate(dto.residenceAddress());

        University university =
            serviceUniversity.findById(dto.universityId());

        repository.save(
            Student.builder()
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

                .password(
                    passwordEncoder.encode(dto.password())
                )

                .secondLanguage(dto.secondLanguage())

                .academicPrograms(dto.academicPrograms())

                .studentStatus(dto.studentStatus())

                .courseApproved(dto.courseApproved())

                .entryDateAcademicProgram(
                    dto.entryDateAcademicProgram()
                )

                .startInductionDate(
                    dto.startInductionDate()
                )

                .endInductionDate(
                    dto.endInductionDate()
                )

                .arlStartDate(dto.arlStartDate())

                .arlEndDate(dto.arlEndDate())

                .hobbies(dto.hobbies())

                .university(university)

                .build()
        );
    }

    @Transactional
    public void delete(String id){

        Student student = repository.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Student not found with id: " + id
                )
            );

        repository.delete(student);
    }

    @Transactional
    public void patch(String id, JsonNode node){

        Student student = repository.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Student not found with id: " + id
                )
            );

        try {

            objectMapper
                .readerForUpdating(student)
                .readValue(node);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        repository.save(student);
    }
}