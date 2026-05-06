package com.usta.edu.co.MedicineRotationManager.models;

import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_academic_periods")
public class StudentAcademicPeriod {
    @Column(name = "academic_period_id", nullable = false, length = 100)
    private String academicPeriodId;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @OneToMany
    @JoinColumn(name = "academic_pedriod_Class_id", nullable = false)
    private AcademicPeriod academicPeriodClass;
    @Enumerated(EnumType.STRING)
    @Column(name = "semester", nullable = false)
    private Semester semester;

}
