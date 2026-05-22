package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.EcxelCreateStudentDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentUpdateDTO;
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

    private AuthUserService userService;

    private ServiceUniversity serviceUniversity;

    public ServiceStudent(StudentRepository repository, ObjectMapper objectMapper, ServiceLocation serviceLocation,
            AuthUserService userService, ServiceUniversity serviceUniversity) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.serviceLocation = serviceLocation;
        this.userService = userService;
        this.serviceUniversity = serviceUniversity;
    }

    public Student findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found with id: " + id));
    }

    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Student> findCloseToExpireARL() {
        return repository.findByCloseToExpireArl();
    }

    @Transactional
    public void save(StudentCreateDTO dto) {

        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());

        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());

        University university = serviceUniversity.findById(dto.universityId());
        Student student = Student.builder()
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

                .secondLanguage(dto.secondLanguage())

                .academicPrograms(dto.academicPrograms())

                .studentStatus(dto.studentStatus())

                .courseApproved(dto.courseApproved())

                .entryDateAcademicProgram(
                        dto.entryDateAcademicProgram())

                .startInductionDate(
                        dto.startInductionDate())

                .endInductionDate(
                        dto.endInductionDate())

                .arlStartDate(dto.arlStartDate())

                .arlEndDate(dto.arlEndDate())

                .hobbies(dto.hobbies())

                .university(university)

                .build();
        student = repository.save(
                student);
        userService.createUser(student, dto.password());
    }

    @Transactional
    public void save(EcxelCreateStudentDTO dto) {

        Location placeBirth = serviceLocation.findOrCreate(dto.getPlaceBirthCity(), dto.getPlaceBirthDepartment(),
                dto.getPlaceBirthAddress());

        Location residenceAddress = serviceLocation.findOrCreate(dto.getResidenceCity(), dto.getResidenceDepartment(),
                dto.getResidenceAddress());

        University university = serviceUniversity.findById(dto.getUniversityId());
        Student student = Student.builder()
                .id(UUIDGenerator.generateNewId())

                .name(dto.getName())
                .lastName(dto.getLastName())
                .dni(dto.getDni())

                .maritalStatus(dto.getMaritalStatus())

                .placeBirth(placeBirth)
                .residenceAddress(residenceAddress)

                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())

                .typeBlood(dto.getTypeBlood())

                .weight(dto.getWeight())
                .imc(dto.getImc())

                .secondLanguage(dto.getSecondLanguage())

                .academicPrograms(dto.getAcademicPrograms())

                .studentStatus(dto.getStudentStatus())

                .courseApproved(dto.isCourseApproved())

                .entryDateAcademicProgram(
                        dto.getEntryDateAcademicProgram())

                .startInductionDate(
                        dto.getStartInductionDate())

                .endInductionDate(
                        dto.getEndInductionDate())

                .arlStartDate(dto.getArlStartDate())

                .arlEndDate(dto.getArlEndDate())

                .hobbies(dto.getHobbies())

                .university(university)

                .build();
        student = repository.save(
                student);
        userService.createUser(student, dto.getPassword());
    }

    @Transactional
    public void delete(String id) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found with id: " + id));

        repository.delete(student);
    }

    @Transactional
    public void patch(String id, JsonNode node) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found with id: " + id));

        try {

            objectMapper
                    .readerForUpdating(student)
                    .readValue(node);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        repository.save(student);
    }

    @Transactional
    public void update(String id, StudentUpdateDTO dto) {

        Student student = findById(id);

        University university = serviceUniversity.findById(dto.universityId());

        student.setName(dto.name());
        student.setLastName(dto.lastName());
        student.setDni(dto.dni());
        student.setMaritalStatus(dto.maritalStatus());
        student.setPhoneNumber(dto.phoneNumber());
        student.setEmail(dto.email());
        student.setTypeBlood(dto.typeBlood());
        student.setWeight(dto.weight());
        student.setImc(dto.imc());

        student.setSecondLanguage(dto.secondLanguage());
        student.setAcademicPrograms(dto.academicPrograms());
        student.setStudentStatus(dto.studentStatus());

        student.setCourseApproved(dto.courseApproved());

        student.setEntryDateAcademicProgram(
                dto.entryDateAcademicProgram());

        student.setStartInductionDate(
                dto.startInductionDate());

        student.setEndInductionDate(
                dto.endInductionDate());

        student.setArlStartDate(dto.arlStartDate());

        student.setArlEndDate(dto.arlEndDate());

        student.setHobbies(dto.hobbies());

        student.setUniversity(university);

        repository.save(student);
    }
}