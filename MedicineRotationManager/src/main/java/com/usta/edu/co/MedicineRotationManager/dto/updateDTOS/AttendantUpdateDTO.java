package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.TypeAttendant;

public record AttendantUpdateDTO(String name, String lastName, String phoneNumber, String dni,TypeAttendant typeAttendant) {

}
