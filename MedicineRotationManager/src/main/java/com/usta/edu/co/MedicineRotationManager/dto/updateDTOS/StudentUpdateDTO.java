package com.usta.edu.co.MedicineRotationManager.dto.updateDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StudentUpdateDTO(

        @Size(min = 2, max = 100)
        String name,

        @Size(min = 2, max = 100)
        String lastName,

        @Pattern(regexp = "^\\d+$")
        String dni,

        MaritalStatus maritalStatus,

        @Pattern(regexp = "^\\d+$")
        String phoneNumber,

        @Email
        String email,

        TypeBlood typeBlood,

        @Positive
        Double weight,

        @Positive
        Double imc,

        Language secondLanguage,

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

        @Pattern(regexp = "^\\d+$")
        String universityId

) {
}