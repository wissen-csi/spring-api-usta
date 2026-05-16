package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entry")
public class Entry {
    @Id
    private String id;

    @CreationTimestamp
    @Column(name = "assistance", nullable = false, updatable = false)
    private LocalDateTime assistance;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
