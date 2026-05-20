package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

public record StudentCreateDTO(

        String name,

        String lastName,

        String dni,

        MaritalStatus maritalStatus,

        LocationCreateDTO placeBirth,

        LocationCreateDTO residenceAddress,

        String phoneNumber,

        String email,

        TypeBlood typeBlood,

        double weight,

        double imc,

        String password,//

        Language secondLanguage,

        AcademicPrograms academicPrograms,

        StudentStatus studentStatus,

        boolean courseApproved,

        LocalDate entryDateAcademicProgram,

        LocalDate startInductionDate,

        LocalDate endInductionDate,

        LocalDate arlStartDate,

        LocalDate arlEndDate,

        String hobbies,

        String universityId

) {

}