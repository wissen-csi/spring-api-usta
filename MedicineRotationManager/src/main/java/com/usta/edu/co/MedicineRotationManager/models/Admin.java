package com.usta.edu.co.MedicineRotationManager.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "admins")
public class Admin extends Person {

    @OneToMany(mappedBy = "admin")
    private List<Student> studentManagement;

    @OneToMany(mappedBy = "admin")
    private List<Doctor> doctorManagement;

    @OneToMany(mappedBy = "admin")
    private List<Task> tasks;

}