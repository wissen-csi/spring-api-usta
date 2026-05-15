package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends Person {
    @Column(name = "hiring_date",nullable = false)
    private LocalDate hiringDate;
    @Column(name = "end_Date", nullable = false)
    private LocalDate endDate;
    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Task> tasks;

}