package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.StudentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.services.ServiceStudent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/student")
public class StudentController {
    private ServiceStudent serviceStudent;

    public StudentController(ServiceStudent serviceStudent) {
        this.serviceStudent = serviceStudent;
    }


    

}
