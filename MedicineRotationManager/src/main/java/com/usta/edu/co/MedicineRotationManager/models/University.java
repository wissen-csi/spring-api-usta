package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Builder
@Entity
@Table(name = "universities")
public class University {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Location address;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false,length = 150)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;
    @Builder.Default
    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
    private List<Doctor> teachers = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<Student> students = new ArrayList<>();
}