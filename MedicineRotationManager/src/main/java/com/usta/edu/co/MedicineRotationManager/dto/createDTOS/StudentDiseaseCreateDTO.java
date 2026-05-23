package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

public record StudentDiseaseCreateDTO(

        String studentId,

        DiseaseCieDTO diseaseCieDTO,

        Boolean isActive

) {
}
