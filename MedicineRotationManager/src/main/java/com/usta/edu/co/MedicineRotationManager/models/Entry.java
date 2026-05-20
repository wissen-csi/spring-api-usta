package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.usta.edu.co.MedicineRotationManager.enumerations.StatusEntry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "entry")
public class Entry {
    @Id
    private String id;

    @Enumerated
    private StatusEntry statusEntry;

    @CreationTimestamp
    @Column(name = "assistance", nullable = false, updatable = false)
    private LocalDateTime assistance;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @JoinColumn(name = "entryPractice_id",nullable = false)
    private EntryPractice entryPractice;
}
