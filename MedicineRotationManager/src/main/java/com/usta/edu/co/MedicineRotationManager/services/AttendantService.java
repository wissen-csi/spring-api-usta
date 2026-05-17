package com.usta.edu.co.MedicineRotationManager.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AttendantCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AttendantUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Attendant;
import com.usta.edu.co.MedicineRotationManager.models.Attendant.AttendantBuilder;
import com.usta.edu.co.MedicineRotationManager.models.University;
import com.usta.edu.co.MedicineRotationManager.repositories.AttendantRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttendantService {

    private final AttendantRepository attendantRepository;
    private final ServiceStudent serviceStudent;

    @Transactional
    public void saveAttendant(AttendantCreateDTO attendantCreateDTO) {
        serviceStudent.findById(attendantCreateDTO.studentId());
        Attendant attendant = Attendant.builder()
                .id(UUIDGenerator.generateNewId())
                .name(attendantCreateDTO.name())
                .lastName(attendantCreateDTO.name())
                .dni(attendantCreateDTO.dni())
                .phoneNumber(attendantCreateDTO.phoneNumber())
                .typeRelative(attendantCreateDTO.typeAttendant())
                .build();
        this.attendantRepository.save(attendant);
    }

    @Transactional
    public void delete(String id) {
        attendantRepository.findById(id);
    }

    

}
