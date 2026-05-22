package com.usta.edu.co.MedicineRotationManager.dto.responseDTOS;

import java.time.LocalDate;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;

import lombok.Builder;
@Builder
public record StudentResponseDTO(

        String id,

        String fullName,

        String dni,

        String email,

        String phoneNumber,

        AcademicPrograms academicProgram,

        StudentStatus studentStatus,

        Language secondLanguage,

        boolean courseApproved,

        LocalDate arlEndDate,

        String universityName

) {
}