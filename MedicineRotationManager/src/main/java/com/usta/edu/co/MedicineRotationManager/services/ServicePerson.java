package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.repositories.PersonRepository;

@Service
public class ServicePerson {
private PersonRepository personRepository;

public ServicePerson(PersonRepository personRepository) {
    this.personRepository = personRepository;
}
public List<String> findEmailsByRole(AppRole role){
    return personRepository.findEmailsByRole(role.name());
}
}
