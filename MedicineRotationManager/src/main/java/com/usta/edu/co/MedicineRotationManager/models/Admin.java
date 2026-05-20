package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "admins")

public class Admin extends Person {
    @Column(name = "hiring_date",nullable = false)
    private LocalDate hiringDate;
    @Column(name = "end_Date", nullable = false)
    private LocalDate endDate;
    @Builder.Default
    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Task> tasks= new ArrayList<>() ;
    public Admin(){}

}