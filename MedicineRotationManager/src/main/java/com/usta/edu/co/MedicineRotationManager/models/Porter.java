package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "porter")

public class Porter extends Person {

    @Column(name = "employeeCode", nullable = false, length = 100, unique = true)
    private String employeeCode;
    @Column(name = "hireDate", nullable = false, unique = true)
    private LocalDate hireDate;
    @Column(name = "isActive", nullable = false, unique = true)
    private boolean isActive;

}
