package com.usta.edu.co.MedicineRotationManager.services;

import com.usta.edu.co.MedicineRotationManager.models.Attendant;
import com.usta.edu.co.MedicineRotationManager.repositories.AttendantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttendantService {

    private final AttendantRepository attendantRepository;

    public void saveAttendant(Attendant attendant) {
        attendantRepository.save(attendant);
    }

    public void updateAttendant(Attendant attendant) {

    }
}
