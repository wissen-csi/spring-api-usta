package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCieDTO;

public record StudentDiseaseUpdateDTO(

        String studentId,

        DiseaseCieDTO diseaseCieDTO,

        Boolean isActive

) {
}