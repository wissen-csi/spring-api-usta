package com.usta.edu.co.MedicineRotationManager.models;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.usta.edu.co.MedicineRotationManager.enumerations.MaritalEstatus;
import com.usta.edu.co.MedicineRotationManager.enumerations.RoleApp;
import com.usta.edu.co.MedicineRotationManager.enumerations.TypeBlood;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "people")
public abstract class Person {
@Id
private String id;
@CreationTimestamp
@Column(name = "creation_date",nullable = false)
private LocalDate creationDate;
@Column(name="last_update",nullable = true)
private LocalDate lastUpdate;
@Column(nullable = false,length = 50)
private String name;
@Column(name = "last_name",nullable = false,length = 50)
private String lastName;
@Column(nullable = false,length = 50)
private String dni;
@Column(name = "marital_estatus",nullable = false)
private MaritalEstatus maritalEstatus;
@ManyToOne
@JoinColumn(name ="place_birth_id")
private Location placeBirth;
@ManyToOne
@JoinColumn(name = "residence_address_id")
private Location residenceAddress;
@Column(name = "phone_number",nullable = false,length =11 )
private String phoneNumber;
@Column(nullable = false,length = 150)
private String email;
@Enumerated(EnumType.STRING)
private TypeBlood typeBlood;
@Column
private double weight;
@Column
private double imc;
@Column(name = "photo_url")
private String photoUrl;
@Enumerated(EnumType.STRING)
private RoleApp role;
}
