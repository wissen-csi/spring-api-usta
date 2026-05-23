package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @OneToMany(mappedBy = "academicPeriod", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.REMOVE } ,orphanRemoval = true)
    private List<StudentAcademicPeriod> studentAcademicPeriod;

}
