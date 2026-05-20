package com.usta.edu.co.MedicineRotationManager.models;

import java.util.ArrayList;
import java.util.List;

import com.usta.edu.co.MedicineRotationManager.enumerations.Specialty;

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
@SuperBuilder
@Entity
@Table(name = "doctors")
public class Doctor extends Person {

    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", nullable = false)
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;
    @Builder.Default
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Rotation> rotations = new ArrayList
    <>();
    

}