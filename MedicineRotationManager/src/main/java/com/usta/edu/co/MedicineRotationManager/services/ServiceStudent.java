package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.RepositoriStudent;

import jakarta.persistence.EntityNotFoundException;
@Service
public class ServiceStudent {

    private RepositoriStudent repositoriStudent;

    
    public ServiceStudent(RepositoriStudent repositoriStudent) {
        this.repositoriStudent = repositoriStudent;
    }


    public Student findbyId(String id){
        return repositoriStudent.findById(id).orElseThrow(()-> new EntityNotFoundException()); 
    }


    public List<Student> findCloseToExpireARL(){
        return repositoriStudent.findByCloseToExpireArl();
    }

}
