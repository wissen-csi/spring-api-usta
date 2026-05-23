package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record StudentResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String fullName,
        @NotBlank
        String dni,
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotNull
        AcademicPrograms academicProgram,
        @NotNull
        StudentStatus studentStatus,
        @NotNull
        Language secondLanguage,
        @NotNull
        boolean courseApproved,
        @NotNull
        LocalDate arlEndDate,
        @NotBlank
        String universityName

) {
}