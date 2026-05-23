package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

public record FileCreateDTO(

        String publicId,

        String secureUrl,

        String originalName,

        String format,

        String resourceType,

        Long size,

        String personId

) {
}