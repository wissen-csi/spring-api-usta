package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record StudentCreateDTO(

        @NotBlank
 
        String name,

        @NotBlank

        String lastName,

        @NotBlank
        @Pattern(regexp = "^\\d+$")
        String dni,

        @NotNull
        MaritalStatus maritalStatus,

        @NotNull
        @Valid
        LocationCreateDTO placeBirth,

        @NotNull
        @Valid
        LocationCreateDTO residenceAddress,

        @NotBlank
        @Pattern(regexp = "^\\d+$")
        String phoneNumber,

        @NotBlank
        @Email
        String email,

        @NotNull
        TypeBlood typeBlood,

        @NotNull
        @Positive
        Double weight,

        @NotNull
        @Positive
        Double imc,

        @NotBlank

        String password,

        @NotNull
        Language secondLanguage,

        @NotNull
        AcademicPrograms academicPrograms,

        @NotNull
        StudentStatus studentStatus,

        @NotNull
        Boolean courseApproved,

        @NotNull
        LocalDate entryDateAcademicProgram,

        @NotNull
        LocalDate startInductionDate,

        @NotNull
        LocalDate endInductionDate,

        @NotNull
        LocalDate arlStartDate,

        @NotNull
        LocalDate arlEndDate,

        @NotBlank
        @Size(max = 500)
        String hobbies,

        @NotBlank
        @Pattern(regexp = "^\\d+$")
        String universityId

) {
}