package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "academic_periods")
public class AcademicPeriod {
    @Id
    private String id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "is_Active", nullable = false)
    private boolean isActive;
    @OneToMany(mappedBy = "academicPeriod")
    private List<StudentAcademicPeriod> studentAcademicPeriod;
}
