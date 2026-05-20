package com.usta.edu.co.MedicineRotationManager.services;

import com.usta.edu.co.MedicineRotationManager.repositories.StudentDiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDisease {
    private final StudentDiseaseRepository studentDiseaseRepository;
    private final CieService cieService;

}
