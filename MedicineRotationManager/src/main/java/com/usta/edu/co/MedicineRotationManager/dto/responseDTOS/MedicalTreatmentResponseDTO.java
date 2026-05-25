package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Jacksonized
@Builder
@RequiredArgsConstructor
public class MedicalTreatmentResponseDTO {
    private final String medicineTreatmentId;
    private final String title;
    private final String medicineId;
    private final String medicineName;
    private final String medicineGramaje;
    private final String studentId;
    private final String studentDni;
    private final LocalDate startMedication;
    private final LocalDate endMedication;
}
