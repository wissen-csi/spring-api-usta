package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.services.ServiceRotation;

@RestController
@RequestMapping("/rotation")
public class RotationController {
private  ServiceRotation serviceRotation;

public RotationController(ServiceRotation serviceRotation) {
    this.serviceRotation = serviceRotation;
}

}
