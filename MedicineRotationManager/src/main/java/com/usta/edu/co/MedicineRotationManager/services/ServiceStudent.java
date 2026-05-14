package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class ServiceStudent {

    private StudentRepository studentRepository;

    
    public ServiceStudent(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student findById(String id){
        return studentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException()); 
    }


    public List<Student> findCloseToExpireARL(){
        return studentRepository.findByCloseToExpireArl();
    }
    public List<Student> findAll(){
        return studentRepository.findAll();
    }

}
