package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.repositories.PersonRepositori;

@Service
public class ServicePerson {
private PersonRepositori personRepositori;

public ServicePerson(PersonRepositori personRepositori) {
    this.personRepositori = personRepositori;
}
public List<String> findEmailsByRole(AppRole role){
    return personRepositori.findEmailsByRole(role);
}
}
