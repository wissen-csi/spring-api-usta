package com.usta.edu.co.MedicineRotationManager.controllers;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.StudentResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.services.ServiceStudent;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/student")
public class StudentController {
    private ServiceStudent serviceStudent;

    public StudentController(ServiceStudent serviceStudent) {
        this.serviceStudent = serviceStudent;
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN')")
    
    public ResponseEntity<Page<StudentResponseDTO>> findAll(Pageable pageable){
        Page<Student> responseModels= serviceStudent.findAll(pageable);
        Page<StudentResponseDTO> response = responseModels.map(x -> StudentResponseDTO.builder()
        .id(x.getId())
        .fullName(x.getName()+" "+x.getLastName())
        .dni(x.getDni())
        .email(x.getEmail())
        .phoneNumber(x.getPhoneNumber())
        .academicProgram(x.getAcademicPrograms())
        .studentStatus(x.getStudentStatus())
        .secondLanguage(x.getSecondLanguage())
        .courseApproved(x.isCourseApproved())
        .arlEndDate(x.getArlEndDate())
        .universityName(x.getUniversity().getName())
        .build()
        );
        return ResponseEntity.ok(response);
   
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<StudentResponseDTO> findById(@PathVariable String id){
        Student student = serviceStudent.findById(id);
        return ResponseEntity.ok(StudentResponseDTO.builder()
        .id(student.getId())
        .fullName(student.getName()+" "+student.getLastName())
        .dni(student.getDni())
        .email(student.getEmail())
        .phoneNumber(student.getPhoneNumber())
        .academicProgram(student.getAcademicPrograms())
        .studentStatus(student.getStudentStatus())
        .secondLanguage(student.getSecondLanguage())
        .courseApproved(student.isCourseApproved())
        .arlEndDate(student.getArlEndDate())
        .universityName(student.getUniversity().getName())
        .build()
    );
    }

    @GetMapping("/find/self")
    @PreAuthorize("hasRole('STUDENT')")

    public ResponseEntity<StudentResponseDTO> findById(@AuthenticationPrincipal AuthUser user){
        Student student = serviceStudent.findById(user.getId());
        return ResponseEntity.ok(StudentResponseDTO.builder()
        .id(student.getId())
        .fullName(student.getName()+" "+student.getLastName())
        .dni(student.getDni())
        .email(student.getEmail())
        .phoneNumber(student.getPhoneNumber())
        .academicProgram(student.getAcademicPrograms())
        .studentStatus(student.getStudentStatus())
        .secondLanguage(student.getSecondLanguage())
        .courseApproved(student.isCourseApproved())
        .arlEndDate(student.getArlEndDate())
        .universityName(student.getUniversity().getName())
        .build()
    );
    }


    @GetMapping("/find/close/arl")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<StudentResponseDTO>> findCloseToExpireARL(){
        List<StudentResponseDTO> response = serviceStudent.findCloseToExpireARL().stream().map(x -> StudentResponseDTO.builder()
        .id(x.getId())
        .fullName(x.getName()+" "+x.getLastName())
        .dni(x.getDni())
        .email(x.getEmail())
        .phoneNumber(x.getPhoneNumber())
        .academicProgram(x.getAcademicPrograms())
        .studentStatus(x.getStudentStatus())
        .secondLanguage(x.getSecondLanguage())
        .courseApproved(x.isCourseApproved())
        .arlEndDate(x.getArlEndDate())
        .universityName(x.getUniversity().getName())
        .build()
        ).toList();
        return ResponseEntity.ok(response);
        
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody StudentCreateDTO dto){
        serviceStudent.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id){
        serviceStudent.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> update(@RequestBody StudentUpdateDTO dto, @PathVariable String id){
        serviceStudent.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/self")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> update(@RequestBody StudentUpdateDTO dto, @AuthenticationPrincipal AuthUser user){
        serviceStudent.update(user.getId(), dto);
        return ResponseEntity.noContent().build();
    }
    


}
