package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepositori;

import jakarta.persistence.EntityNotFoundException;
@Service
public class ServiceStudent {

    private StudentRepositori repositoriStudent;

    
    public ServiceStudent(StudentRepositori repositoriStudent) {
        this.repositoriStudent = repositoriStudent;
    }


    public Student findById(String id){
        return repositoriStudent.findById(id).orElseThrow(()-> new EntityNotFoundException()); 
    }


    public List<Student> findCloseToExpireARL(){
        return repositoriStudent.findByCloseToExpireArl();
    }
    public List<Student> findAll(){
        return repositoriStudent.findAll();
    }

}
