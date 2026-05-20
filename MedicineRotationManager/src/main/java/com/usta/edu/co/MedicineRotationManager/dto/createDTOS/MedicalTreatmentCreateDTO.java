package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

public record MedicalTreatmentCreateDTO(String medicineId, String studentId, LocalDate startMedication,
        LocalDate endMedication) {

}
