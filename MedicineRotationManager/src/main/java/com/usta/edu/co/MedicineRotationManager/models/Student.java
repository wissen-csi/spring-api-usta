package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicPrograms;
import com.usta.edu.co.MedicineRotationManager.enumerations.Language;
import com.usta.edu.co.MedicineRotationManager.enumerations.StudentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "students")
public class Student extends Person {

    @Enumerated(EnumType.STRING)
    @Column(name = "second_language", nullable = false)
    private Language secondLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_program", nullable = false)
    private AcademicPrograms academicPrograms;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status", nullable = false)
    private StudentStatus studentStatus;

    @Column(name = "course_approved", nullable = false)
    private boolean courseApproved;

    @Column(name = "entry_date_academic_program", nullable = false)
    private LocalDate entryDateAcademicProgram;

    @Column(name = "start_induction_date", nullable = true)
    private LocalDate startInductionDate;

    @Column(name = "end_induction_date", nullable = true)
    private LocalDate endInductionDate;

    @Column(name = "arl_start_date", nullable = false)
    private LocalDate arlStartDate;

    @Column(name = "arl_end_date", nullable = false)
    private LocalDate arlEndDate;

    @Column(name = "hobbies", nullable = false, columnDefinition = "TEXT")
    private String hobbies;


    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Attendant> relatives = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<MedicationTreatment> medicationTreatments = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<StudentDisease> studentDiseases = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<ResearchParticipant> researchParticipants = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student" ,fetch = FetchType.LAZY)
    private List<GroupAssignment> groupAssignments = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<StudentAcademicPeriod> studentAcademicPeriods = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;



}