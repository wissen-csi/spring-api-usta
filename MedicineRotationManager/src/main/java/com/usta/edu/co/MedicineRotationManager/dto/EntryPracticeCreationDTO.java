package com.usta.edu.co.MedicineRotationManager.dto;

import java.time.LocalDateTime;

public record EntryPracticeCreationDTO(LocalDateTime starTime, LocalDateTime endTime,String idGroup) {
    
}
