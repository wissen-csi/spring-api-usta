package com.usta.edu.co.MedicineRotationManager.models;

import com.usta.edu.co.MedicineRotationManager.enumerations.AcademicConnection;
import com.usta.edu.co.MedicineRotationManager.enumerations.Semester;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_academic_periods")
public class StudentAcademicPeriod {
    @Id
    private String id;
    @Column(name = "academic_period_id", nullable = false, length = 100)
    private String academicPeriodId;
    @Column(name = "cumulative_average", nullable = false)
    private double cumulativeAverage;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "academic_pedriod_id", nullable = false)
    private AcademicPeriod academicPeriod;
    @Enumerated(EnumType.STRING)
    @Column(name = "semester", nullable = false)
    private Semester semester;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "academic_connection", nullable = false)
    private AcademicConnection academicConnection;
}
