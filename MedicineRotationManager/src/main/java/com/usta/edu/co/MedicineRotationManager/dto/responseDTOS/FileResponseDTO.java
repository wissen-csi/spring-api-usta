package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class FileResponseDTO {

    private final String id;

    private final String publicId;

    private final String secureUrl;

    private final String originalName;

    private final String format;

    private final String resourceType;

    private final Long size;

    private final String personId;
}