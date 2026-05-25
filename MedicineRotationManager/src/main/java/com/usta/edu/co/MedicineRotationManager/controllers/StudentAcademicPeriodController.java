package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentAcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.StudentAcademicPeriodResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentAcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.StudentAcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.services.ServiceStudentAcademicPeriod;

@RestController
@RequestMapping("/StudentAcademicPeriod")
public class StudentAcademicPeriodController {
    private ServiceStudentAcademicPeriod serviceStudentAcademicPeriod;

    public StudentAcademicPeriodController(ServiceStudentAcademicPeriod serviceStudentAcademicPeriod) {
        this.serviceStudentAcademicPeriod = serviceStudentAcademicPeriod;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Void> create(@RequestBody StudentAcademicPeriodCreateDTO dto){
        serviceStudentAcademicPeriod.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody StudentAcademicPeriodUpdateDTO dto){
        serviceStudentAcademicPeriod.update(dto, id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/find/all")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','DOCTOR')")
    public ResponseEntity<Page<StudentAcademicPeriodResponseDTO>> findAll(Pageable pageable){
        Page<StudentAcademicPeriod> page = serviceStudentAcademicPeriod.findAll(pageable);
        Page<StudentAcademicPeriodResponseDTO> response = page.map(x->StudentAcademicPeriodResponseDTO.builder()
    .id(x.getId())
    .cumulativeAverage(x.getCumulativeAverage())
    .studentName(x.getStudent().getName()+" "+x.getStudent().getLastName())
    .academicPeriodName(x.getAcademicPeriod().getName())
    .semester(x.getSemester())
    .academicConnection(x.getAcademicConnection())
    .build()
    );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','DOCTOR')")
    public ResponseEntity<StudentAcademicPeriodResponseDTO> findById(@PathVariable String id){
        StudentAcademicPeriod studentAcademicPeriod = serviceStudentAcademicPeriod.findById(id);
        return ResponseEntity.ok(StudentAcademicPeriodResponseDTO.builder()
    .id(studentAcademicPeriod.getId())
    .cumulativeAverage(studentAcademicPeriod.getCumulativeAverage())
    .studentName(studentAcademicPeriod.getStudent().getName()+" "+studentAcademicPeriod.getStudent().getLastName())
    .academicPeriodName(studentAcademicPeriod.getAcademicPeriod().getName())
    .semester(studentAcademicPeriod.getSemester())
    .academicConnection(studentAcademicPeriod.getAcademicConnection())
    .build());
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id){
        serviceStudentAcademicPeriod.delete(id);
        return ResponseEntity.noContent().build();
    }
}
