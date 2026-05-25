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

        TypeBlood typeBlood,

        Double weight,

        Double imc,

        @NotBlank
        String password,

        Language secondLanguage,

        @NotNull
        AcademicPrograms academicPrograms,

        StudentStatus studentStatus,

        Boolean courseApproved,

        LocalDate entryDateAcademicProgram,

        LocalDate startInductionDate,

        LocalDate endInductionDate,

        LocalDate arlStartDate,

        LocalDate arlEndDate,

        @Size(max = 500)
        String hobbies,

        @NotBlank
        String universityId

) {
}
