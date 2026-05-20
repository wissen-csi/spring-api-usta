package com.usta.edu.co.MedicineRotationManager.models;

import java.util.ArrayList;
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
@Entity
@Builder
@Table(name = "rotation_groups")
public class Group {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "rotation_id", nullable = false)
    private Rotation rotation;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;
    @Builder.Default
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupAssignment> groupAssignments= new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "group",fetch=FetchType.LAZY)
    private List<EntryPractice> entryPractices = new ArrayList<>();
    

}