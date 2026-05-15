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
@Table(name = "medicines")
public class Medicine {

    @Id
    private String id;

    @Column(name = "active_ingredient", nullable = false, length = 150)
    private String activeIngredient;

    @Column(name = "atc", nullable = false, length = 10)
    private String atc;

    @Column(name = "description_atc", nullable = false, columnDefinition = "TEXT")
    private String descriptionAtc;

    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<MedicationTreatment> medicationTreatments;
}