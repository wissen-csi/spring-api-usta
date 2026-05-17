package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import lombok.Getter;

public record DiseaseCreateDTO(@Getter String id,@Getter String code,@Getter String definition, @Getter String name) {
}
