package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "entry_practices")
public class EntryPractice {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "group_assignment_id", nullable = false)
    private GroupAssignment groupAssignment;

    @CreationTimestamp
    @Column(name = "assistance", nullable = false, updatable = false)
    private LocalDateTime assistance;

    @Column(name = "qr_code", nullable = false, length = 255)
    private String qrCode;
}