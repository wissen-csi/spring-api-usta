package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalStatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "people")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person  {

    @Id
    private String id;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "dni", nullable = false, length = 50, unique = true)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @ManyToOne
    @JoinColumn(name = "place_birth_id", nullable = false)
    private Location placeBirth;

    @ManyToOne
    @JoinColumn(name = "residence_address_id", nullable = false)
    private Location residenceAddress;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;
    
    @Column(name = "email", nullable = false, length = 150, unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_blood", nullable = false)
    private TypeBlood typeBlood;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "imc", nullable = false)
    private double imc;

    @Builder.Default
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<File> file = new ArrayList<>();



    public Person(){}

}