package com.usta.edu.co.MedicineRotationManager.dto;

import java.time.LocalDate;

public record MessageStudentDTO(String name, String dni, LocalDate endDate, boolean status) {

}
