package com.usta.edu.co.MedicineRotationManager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "locations")
public class Location {

    @Id
    private String id;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "department", nullable = false, length = 30)
    private String department;
}