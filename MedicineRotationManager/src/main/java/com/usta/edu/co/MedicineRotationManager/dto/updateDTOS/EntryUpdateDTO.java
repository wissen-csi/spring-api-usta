package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDateTime;

import com.usta.edu.co.MedicineRotationManager.enumerations.StatusEntry;

import jakarta.validation.constraints.NotNull;

public record EntryUpdateDTO(
    @NotNull
    LocalDateTime assistance, 
    @NotNull
    StatusEntry statusEntry) {

}
