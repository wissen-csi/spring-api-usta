package com.usta.edu.co.MedicineRotationManager.dtos;

import lombok.Getter;

public record Err(@Getter int code,@Getter String message, @Getter String status
) {

}
