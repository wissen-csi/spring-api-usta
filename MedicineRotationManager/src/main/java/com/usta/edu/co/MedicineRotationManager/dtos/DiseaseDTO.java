package com.usta.edu.co.MedicineRotationManager.dtos;

import lombok.Getter;

public record DiseaseDTO(@Getter String id,@Getter String code,@Getter String definition, @Getter String name) {
}
