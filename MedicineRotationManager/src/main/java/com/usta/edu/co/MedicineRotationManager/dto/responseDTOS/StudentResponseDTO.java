package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.LocationCreateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StudentResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        String fullName,
        @NotBlank
        String dni,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String phoneNumber,
        MaritalStatus maritalStatus,
        TypeBlood typeBlood,
        Double weight,
        Double imc,
        AcademicPrograms academicProgram,
        StudentStatus studentStatus,
        Language secondLanguage,
        Boolean courseApproved,
        LocalDate entryDateAcademicProgram,
        LocalDate startInductionDate,
        LocalDate endInductionDate,
        LocalDate arlStartDate,
        LocalDate arlEndDate,
        String hobbies,
        String universityName,
        String universityId,
        LocationCreateDTO placeBirth,
        LocationCreateDTO residenceAddress
) {
}