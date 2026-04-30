package com.usta.edu.co.MedicineRotationManager.models;

import java.time.LocalDate;

import org.hibernate.annotations.CurrentTimestamp;

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
@Table(name = "universitys")
public class University {
 @Id
 private String id;
 @ManyToOne
 @JoinColumn(name = "address_id")
 private Location address;
 @Column(nullable = false)
 private String email;
 @Column(name = "phone_number",nullable = false)
 private String phoneNumber;
 @Column(name = "is_active", nullable = false)
 private boolean isActive;
 @CurrentTimestamp
 @Column(name = "creation_date", nullable = false)
 private LocalDate creationDate;

}
