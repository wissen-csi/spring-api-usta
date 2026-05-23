package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

public record InvestigationUpdateDTO(String repositoryUrl, String description, LocalDate publicationLocalDate) {

}
