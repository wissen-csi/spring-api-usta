package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import com.usta.edu.co.MedicineRotationManager.models.Person;
import jakarta.persistence.EntityNotFoundException;
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

public Person findById(String id){
    return this.personRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Persona no encontrada"));
}

public Person findByDni(String dni){
    return this.personRepository.findByDni(dni).orElseThrow(()->new EntityNotFoundException("Persona no encontrada con ese DNI"));
}


}
