package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

public record InvestigationCreateDTO(String repositoryUrl, String description, LocalDate publicationDate,
        String studentId) {

}
