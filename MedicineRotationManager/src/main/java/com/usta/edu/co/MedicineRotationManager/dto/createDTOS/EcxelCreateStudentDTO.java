package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import com.poiji.annotation.ExcelCellName;
import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EcxelCreateStudentDTO extends EcxelCreatePersonDTO {
    @ExcelCellName("second_language")
        private Language secondLanguage;
        @ExcelCellName("academic_programs")
        private AcademicPrograms academicPrograms;
        @ExcelCellName("student_status")
        private StudentStatus studentStatus;
        @ExcelCellName("course_approved")
        private boolean courseApproved;
        @ExcelCellName("entry_dateAcademic_program")
        private LocalDate entryDateAcademicProgram;
        @ExcelCellName("start_induction_date")
        private LocalDate startInductionDate;
        @ExcelCellName("end_induction_date")
        private LocalDate endInductionDate;
        @ExcelCellName("arl_start_date")
        private LocalDate arlStartDate;
        @ExcelCellName("arl_endDate")
        private LocalDate arlEndDate;
        @ExcelCellName("hobbies")
        private String hobbies;
        @ExcelCellName("university_id")
        private String universityId;
}
