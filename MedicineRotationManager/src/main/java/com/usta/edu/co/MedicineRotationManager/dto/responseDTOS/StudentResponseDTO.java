package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@RequiredArgsConstructor
@Getter

public class StudentResponseDTO {
    private final String id;
    private final String fullName;
    private final String dni;
    private final String email;
    private final String phoneNumber;
    private final AcademicPrograms academicProgram;
    private final StudentStatus studentStatus;
    private final Language secondLanguage;
    private final boolean courseApproved;
    private final LocalDate arlEndDate;
    private final String universityName;
}