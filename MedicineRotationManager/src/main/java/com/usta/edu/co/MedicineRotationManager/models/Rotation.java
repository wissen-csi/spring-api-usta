package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.usta.edu.co.MedicineRotationManager.enumerations.HospitalLocation;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeRotation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
@Builder
@Table(name = "rotations")
public class Rotation {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "hospital_location", nullable = false)
    private HospitalLocation hospitalLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_rotation", nullable = false)
    private TypeRotation typeRotation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;



    @Builder.Default
    @OneToMany(mappedBy = "rotation", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<Group> groups= new ArrayList<>();
}