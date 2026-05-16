package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.TypeAttendant;

public record AttendantCreateDTO(String name, String lastName, String phoneNumber, TypeAttendant typeAttendant,
        String dni) {

}
