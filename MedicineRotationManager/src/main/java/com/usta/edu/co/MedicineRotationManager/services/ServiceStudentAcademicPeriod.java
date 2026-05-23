package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentAcademicPeriodCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.StudentAcademicPeriodUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.AcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.models.StudentAcademicPeriod;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentAcademicPeriodRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceStudentAcademicPeriod {
    private StudentAcademicPeriodRepository repository;
    private ServiceStudent serviceStudent;
    private AcademicPeriodService academicPeriodService;
    public ServiceStudentAcademicPeriod(StudentAcademicPeriodRepository repository, ServiceStudent serviceStudent,
            AcademicPeriodService academicPeriodService) {
        this.repository = repository;
        this.serviceStudent = serviceStudent;
        this.academicPeriodService = academicPeriodService;
    }
    public Page<StudentAcademicPeriod> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
    public StudentAcademicPeriod findById(String id){
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
    }
    public void delete(String id){
        StudentAcademicPeriod studentAcademicPeriod = repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        repository.delete(studentAcademicPeriod);
    }
    public void save(StudentAcademicPeriodCreateDTO dto){
        Student student = serviceStudent.findById(dto.studentId());
        AcademicPeriod academicPeriod = academicPeriodService.findById(dto.academicPeriodId());
        StudentAcademicPeriod studentAcademicPeriod = StudentAcademicPeriod.builder()
        .id(UUIDGenerator.generateNewId())
        .academicConnection(dto.academicConnection())
        .cumulativeAverage(dto.cumulativeAverage())
        .student(student)
        .academicPeriod(academicPeriod)
        .semester(dto.semester())
        .build();
        repository.save(studentAcademicPeriod);
    }
    public void update(StudentAcademicPeriodUpdateDTO dto, String id){
        StudentAcademicPeriod studentAcademicPeriod = repository.findById(id).orElseThrow(()->new EntityNotFoundException());
        studentAcademicPeriod.setCumulativeAverage(dto.cumulativeAverage());
        studentAcademicPeriod.setSemester(dto.semester());
        studentAcademicPeriod.setAcademicConnection(dto.academicConnection());
        repository.save(studentAcademicPeriod);
    }
}
