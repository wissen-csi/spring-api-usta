package com.usta.edu.co.MedicineRotationManager.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.StudentResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.services.ServiceEcxel;
import com.usta.edu.co.MedicineRotationManager.services.ServiceStudent;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {

    private ServiceStudent serviceStudent;
    private ServiceEcxel serviceEcxel;

    public StudentController(ServiceStudent serviceStudent, ServiceEcxel serviceEcxel) {
        this.serviceStudent = serviceStudent;
        this.serviceEcxel=serviceEcxel;
    }
    
    
    
    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")

    public ResponseEntity<Page<StudentResponseDTO>> findAll(Pageable pageable) {
        Page<Student> responseModels = serviceStudent.findAll(pageable);
        Page<StudentResponseDTO> response = responseModels.map(x -> StudentResponseDTO.builder()
                .id(x.getId())
                .name(x.getName())
                .lastName(x.getLastName())
                .fullName(x.getName() + " " + x.getLastName())
                .dni(x.getDni())
                .email(x.getEmail())
                .phoneNumber(x.getPhoneNumber())
                .maritalStatus(x.getMaritalStatus())
                .typeBlood(x.getTypeBlood())
                .weight(x.getWeight())
                .imc(x.getImc())
                .academicProgram(x.getAcademicPrograms())
                .studentStatus(x.getStudentStatus())
                .secondLanguage(x.getSecondLanguage())
                .courseApproved(x.isCourseApproved())
                .entryDateAcademicProgram(x.getEntryDateAcademicProgram())
                .startInductionDate(x.getStartInductionDate())
                .endInductionDate(x.getEndInductionDate())
                .arlStartDate(x.getArlStartDate())
                .arlEndDate(x.getArlEndDate())
                .hobbies(x.getHobbies())
                .universityName(x.getUniversity().getName())
                .universityId(x.getUniversity().getId())
                .placeBirth(new LocationCreateDTO(x.getPlaceBirth().getAddress(), x.getPlaceBirth().getCity(), x.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(x.getResidenceAddress().getAddress(), x.getResidenceAddress().getCity(), x.getResidenceAddress().getDepartment()))
                .build()
        );
        return ResponseEntity.ok(response);

    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<StudentResponseDTO> findById(@PathVariable String id) {
        Student student = serviceStudent.findById(id);
        return ResponseEntity.ok(StudentResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .fullName(student.getName() + " " + student.getLastName())
                .dni(student.getDni())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .maritalStatus(student.getMaritalStatus())
                .typeBlood(student.getTypeBlood())
                .weight(student.getWeight())
                .imc(student.getImc())
                .academicProgram(student.getAcademicPrograms())
                .studentStatus(student.getStudentStatus())
                .secondLanguage(student.getSecondLanguage())
                .courseApproved(student.isCourseApproved())
                .entryDateAcademicProgram(student.getEntryDateAcademicProgram())
                .startInductionDate(student.getStartInductionDate())
                .endInductionDate(student.getEndInductionDate())
                .arlStartDate(student.getArlStartDate())
                .arlEndDate(student.getArlEndDate())
                .hobbies(student.getHobbies())
                .universityName(student.getUniversity().getName())
                .universityId(student.getUniversity().getId())
                .placeBirth(new LocationCreateDTO(student.getPlaceBirth().getAddress(), student.getPlaceBirth().getCity(), student.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(student.getResidenceAddress().getAddress(), student.getResidenceAddress().getCity(), student.getResidenceAddress().getDepartment()))
                .build()
        );
    }

    @GetMapping("/find/dni/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<StudentResponseDTO> findByDni(@PathVariable String dni) {
        Student student = serviceStudent.findByDni(dni);
        return ResponseEntity.ok(StudentResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .fullName(student.getName() + " " + student.getLastName())
                .dni(student.getDni())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .maritalStatus(student.getMaritalStatus())
                .typeBlood(student.getTypeBlood())
                .weight(student.getWeight())
                .imc(student.getImc())
                .academicProgram(student.getAcademicPrograms())
                .studentStatus(student.getStudentStatus())
                .secondLanguage(student.getSecondLanguage())
                .courseApproved(student.isCourseApproved())
                .entryDateAcademicProgram(student.getEntryDateAcademicProgram())
                .startInductionDate(student.getStartInductionDate())
                .endInductionDate(student.getEndInductionDate())
                .arlStartDate(student.getArlStartDate())
                .arlEndDate(student.getArlEndDate())
                .hobbies(student.getHobbies())
                .universityName(student.getUniversity().getName())
                .universityId(student.getUniversity().getId())
                .placeBirth(new LocationCreateDTO(student.getPlaceBirth().getAddress(), student.getPlaceBirth().getCity(), student.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(student.getResidenceAddress().getAddress(), student.getResidenceAddress().getCity(), student.getResidenceAddress().getDepartment()))
                .build()
        );
    }

    @GetMapping("/find/self")
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<StudentResponseDTO> findById(@AuthenticationPrincipal AuthUser user) {
        Student student = serviceStudent.findById(user.getId());
        return ResponseEntity.ok(StudentResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .fullName(student.getName() + " " + student.getLastName())
                .dni(student.getDni())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .maritalStatus(student.getMaritalStatus())
                .typeBlood(student.getTypeBlood())
                .weight(student.getWeight())
                .imc(student.getImc())
                .academicProgram(student.getAcademicPrograms())
                .studentStatus(student.getStudentStatus())
                .secondLanguage(student.getSecondLanguage())
                .courseApproved(student.isCourseApproved())
                .entryDateAcademicProgram(student.getEntryDateAcademicProgram())
                .startInductionDate(student.getStartInductionDate())
                .endInductionDate(student.getEndInductionDate())
                .arlStartDate(student.getArlStartDate())
                .arlEndDate(student.getArlEndDate())
                .hobbies(student.getHobbies())
                .universityName(student.getUniversity().getName())
                .universityId(student.getUniversity().getId())
                .placeBirth(new LocationCreateDTO(student.getPlaceBirth().getAddress(), student.getPlaceBirth().getCity(), student.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(student.getResidenceAddress().getAddress(), student.getResidenceAddress().getCity(), student.getResidenceAddress().getDepartment()))
                .build()
        );
    }

    @GetMapping("/find/close/arl")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<StudentResponseDTO>> findCloseToExpireARL() {
        List<StudentResponseDTO> response = serviceStudent.findCloseToExpireARL().stream().map(x -> StudentResponseDTO.builder()
                .id(x.getId())
                .name(x.getName())
                .lastName(x.getLastName())
                .fullName(x.getName() + " " + x.getLastName())
                .dni(x.getDni())
                .email(x.getEmail())
                .phoneNumber(x.getPhoneNumber())
                .maritalStatus(x.getMaritalStatus())
                .typeBlood(x.getTypeBlood())
                .weight(x.getWeight())
                .imc(x.getImc())
                .academicProgram(x.getAcademicPrograms())
                .studentStatus(x.getStudentStatus())
                .secondLanguage(x.getSecondLanguage())
                .courseApproved(x.isCourseApproved())
                .entryDateAcademicProgram(x.getEntryDateAcademicProgram())
                .startInductionDate(x.getStartInductionDate())
                .endInductionDate(x.getEndInductionDate())
                .arlStartDate(x.getArlStartDate())
                .arlEndDate(x.getArlEndDate())
                .hobbies(x.getHobbies())
                .universityName(x.getUniversity().getName())
                .universityId(x.getUniversity().getId())
                .placeBirth(new LocationCreateDTO(x.getPlaceBirth().getAddress(), x.getPlaceBirth().getCity(), x.getPlaceBirth().getDepartment()))
                .residenceAddress(new LocationCreateDTO(x.getResidenceAddress().getAddress(), x.getResidenceAddress().getCity(), x.getResidenceAddress().getDepartment()))
                .build()
        ).toList();
        return ResponseEntity.ok(response);

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> create(@Valid @RequestBody StudentCreateDTO dto) {
        String studentId = serviceStudent.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentId);
    }

    @PostMapping(
        value = "/excel/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createByExcel(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        serviceEcxel.createStudents(file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceStudent.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@Valid @RequestBody StudentUpdateDTO dto, @PathVariable String id) {
        serviceStudent.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/self")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Void> update(@Valid @RequestBody StudentUpdateDTO dto, @AuthenticationPrincipal AuthUser user) {
        serviceStudent.update(user.getId(), dto);
        return ResponseEntity.noContent().build();
    }

}
