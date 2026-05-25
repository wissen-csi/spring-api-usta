package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import com.usta.edu.co.MedicineRotationManager.enumerations.StatusEntry;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;


@Builder
@Getter
@RequiredArgsConstructor
@Jacksonized
public class EntryResponseDTO {
    private final String id;
    private final EntryPracticeResponseDTO entryPracticeResponseDTO;
    private final LocalDateTime assistance;
    private final String studentId;
    private final String studentDni;
    private final String studentName;
    private final StatusEntry statusEntry;

}
