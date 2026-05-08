package com.usta.edu.co.MedicineRotationManager.models;

import com.usta.edu.co.MedicineRotationManager.enumerations.Format;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archives")
public class File {

    @Id
    private String id;

    @Column(name = "security_url", nullable = false, columnDefinition = "TEXT")
    private String securityUrl;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Format format;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}