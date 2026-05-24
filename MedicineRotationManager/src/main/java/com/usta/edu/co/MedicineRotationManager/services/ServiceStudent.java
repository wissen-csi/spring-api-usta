package com.usta.edu.co.MedicineRotationManager.services;

import java.time.LocalDate;
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
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;
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

    public Student findByDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found with dni: " + dni));
    }

    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Student> findCloseToExpireARL() {
        return repository.findByCloseToExpireArl();
    }

    @Transactional
    public String save(StudentCreateDTO dto) {

        if (dto.startInductionDate() != null && dto.endInductionDate() != null
                && dto.startInductionDate().isAfter(dto.endInductionDate())) {
            throw new IllegalArgumentException("La fecha de inicio de inducción no puede ser posterior a la fecha de fin");
        }
        if (dto.arlStartDate() != null && dto.arlEndDate() != null
                && dto.arlStartDate().isAfter(dto.arlEndDate())) {
            throw new IllegalArgumentException("La fecha de inicio de ARL no puede ser posterior a la fecha de fin");
        }

        Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());

        Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());

        University university = serviceUniversity.findById(dto.universityId());
        String id = UUIDGenerator.generateNewId();

        MaritalStatus maritalStatus = dto.maritalStatus() != null ? dto.maritalStatus() : MaritalStatus.OTHER;
        TypeBlood typeBlood = dto.typeBlood() != null ? dto.typeBlood() : TypeBlood.O_POSITIVE;
        Language secondLanguage = dto.secondLanguage() != null ? dto.secondLanguage() : Language.OTHER;
        StudentStatus studentStatus = dto.studentStatus() != null ? dto.studentStatus() : StudentStatus.ACTIVE;
        boolean courseApproved = dto.courseApproved() != null ? dto.courseApproved() : false;
        LocalDate entryDateAcademicProgram = dto.entryDateAcademicProgram() != null
                ? dto.entryDateAcademicProgram()
                : LocalDate.now();
        LocalDate arlStartDate = dto.arlStartDate() != null ? dto.arlStartDate()
                : LocalDate.now();
        LocalDate arlEndDate = dto.arlEndDate() != null ? dto.arlEndDate()
                : LocalDate.now().plusYears(1);
        String hobbies = dto.hobbies() != null ? dto.hobbies() : "";
        Double weight = dto.weight() != null ? dto.weight() : 0.0;
        Double imc = dto.imc() != null ? dto.imc() : 0.0;

        Student student = Student.builder()
                .id(id)

                .name(dto.name())
                .lastName(dto.lastName())
                .dni(dto.dni())

                .maritalStatus(maritalStatus)

                .placeBirth(placeBirth)
                .residenceAddress(residenceAddress)

                .phoneNumber(dto.phoneNumber())
                .email(dto.email())

                .typeBlood(typeBlood)

                .weight(weight)
                .imc(imc)

                .secondLanguage(secondLanguage)

                .academicPrograms(dto.academicPrograms())

                .studentStatus(studentStatus)

                .courseApproved(courseApproved)

                .entryDateAcademicProgram(entryDateAcademicProgram)

                .startInductionDate(dto.startInductionDate())

                .endInductionDate(dto.endInductionDate())

                .arlStartDate(arlStartDate)

                .arlEndDate(arlEndDate)

                .hobbies(hobbies)

                .university(university)

                .build();
        student = repository.save(
                student);
        userService.createUser(student, dto.password());
        return id;
    }

    @Transactional
    public void save(EcxelCreateStudentDTO dto) {

        if (dto.getStartInductionDate() != null && dto.getEndInductionDate() != null
                && dto.getStartInductionDate().isAfter(dto.getEndInductionDate())) {
            throw new IllegalArgumentException("La fecha de inicio de inducción no puede ser posterior a la fecha de fin");
        }
        if (dto.getArlStartDate() != null && dto.getArlEndDate() != null
                && dto.getArlStartDate().isAfter(dto.getArlEndDate())) {
            throw new IllegalArgumentException("La fecha de inicio de ARL no puede ser posterior a la fecha de fin");
        }

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

        userService.deleteUser(student);
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

        if (dto.startInductionDate() != null && dto.endInductionDate() != null
                && dto.startInductionDate().isAfter(dto.endInductionDate())) {
            throw new IllegalArgumentException("La fecha de inicio de inducción no puede ser posterior a la fecha de fin");
        }
        if (dto.arlStartDate() != null && dto.arlEndDate() != null
                && dto.arlStartDate().isAfter(dto.arlEndDate())) {
            throw new IllegalArgumentException("La fecha de inicio de ARL no puede ser posterior a la fecha de fin");
        }

        if (dto.name() != null) student.setName(dto.name());
        if (dto.lastName() != null) student.setLastName(dto.lastName());
        if (dto.dni() != null) student.setDni(dto.dni());
        if (dto.maritalStatus() != null) student.setMaritalStatus(dto.maritalStatus());
        if (dto.phoneNumber() != null) student.setPhoneNumber(dto.phoneNumber());
        if (dto.email() != null) student.setEmail(dto.email());
        if (dto.typeBlood() != null) student.setTypeBlood(dto.typeBlood());
        if (dto.weight() != null) student.setWeight(dto.weight());
        if (dto.imc() != null) student.setImc(dto.imc());
        if (dto.secondLanguage() != null) student.setSecondLanguage(dto.secondLanguage());
        if (dto.academicPrograms() != null) student.setAcademicPrograms(dto.academicPrograms());
        if (dto.studentStatus() != null) student.setStudentStatus(dto.studentStatus());
        if (dto.courseApproved() != null) student.setCourseApproved(dto.courseApproved());
        if (dto.entryDateAcademicProgram() != null) student.setEntryDateAcademicProgram(dto.entryDateAcademicProgram());
        if (dto.startInductionDate() != null) student.setStartInductionDate(dto.startInductionDate());
        if (dto.endInductionDate() != null) student.setEndInductionDate(dto.endInductionDate());
        if (dto.arlStartDate() != null) student.setArlStartDate(dto.arlStartDate());
        if (dto.arlEndDate() != null) student.setArlEndDate(dto.arlEndDate());
        if (dto.hobbies() != null) student.setHobbies(dto.hobbies());
        if (dto.universityId() != null) {
            University university = serviceUniversity.findById(dto.universityId());
            student.setUniversity(university);
        }
        if (dto.placeBirth() != null) {
            Location placeBirth = serviceLocation.findOrCreate(dto.placeBirth());
            student.setPlaceBirth(placeBirth);
        }
        if (dto.residenceAddress() != null) {
            Location residenceAddress = serviceLocation.findOrCreate(dto.residenceAddress());
            student.setResidenceAddress(residenceAddress);
        }

        repository.save(student);
    }
}