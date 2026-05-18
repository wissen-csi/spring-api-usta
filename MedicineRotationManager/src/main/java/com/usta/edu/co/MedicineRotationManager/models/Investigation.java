package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;
import java.util.List;

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
@Table(name = "investigations")
public class Investigation {

    @Id
    private String id;

    @Column(name = "repository_url", nullable = false, columnDefinition = "TEXT")
    private String repositoryUrl;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    // Muchas investigaciones pertenecen a un estudiante :)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}