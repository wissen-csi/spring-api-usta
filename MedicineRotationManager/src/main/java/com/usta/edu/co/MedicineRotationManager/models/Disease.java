package com.usta.edu.co.MedicineRotationManager.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "diseases")
public class Disease {

    @Id
    private String id;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "definition", nullable = false, columnDefinition = "TEXT")
    private String definition;

    @OneToMany(mappedBy = "disease", fetch = FetchType.LAZY)
    private List<StudentDisease> studentDiseases;
}