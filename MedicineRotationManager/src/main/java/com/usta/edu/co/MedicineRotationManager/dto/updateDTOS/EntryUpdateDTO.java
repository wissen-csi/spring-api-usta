package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDateTime;

import com.usta.edu.co.MedicineRotationManager.enumerations.StatusEntry;

public record EntryUpdateDTO(LocalDateTime assistance, StatusEntry statusEntry) {

}
