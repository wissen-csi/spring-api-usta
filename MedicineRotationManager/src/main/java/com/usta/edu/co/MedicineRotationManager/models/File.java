package com.usta.edu.co.MedicineRotationManager.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archives")
public class File {

    @Id
    private String id;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String secureUrl;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String format;

    @Column(nullable = false)
    private String resourceType;

    @Column(nullable = false)
    private Long size;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}