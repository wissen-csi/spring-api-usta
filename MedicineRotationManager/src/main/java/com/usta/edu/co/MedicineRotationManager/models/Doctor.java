package com.usta.edu.co.MedicineRotationManager.models;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "doctors")
public class Doctor extends Person {
    @Enumerated(EnumType.STRING)
    private Specialty specialty;
    
}
