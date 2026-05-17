package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.GroupAssignmentCreationDTO;
import com.usta.edu.co.MedicineRotationManager.models.Group;
import com.usta.edu.co.MedicineRotationManager.models.GroupAssignment;
import com.usta.edu.co.MedicineRotationManager.models.Rotation;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.GroupAssignmentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;

public class ServiceGroupAssignment {
    private GroupAssignmentRepository repository;
    private ServiceGroup serviceGroup;
    private ServiceStudent serviceStudent;
    public ServiceGroupAssignment(GroupAssignmentRepository repository, ServiceGroup serviceGroup, ServiceStudent serviceStudent) {
        this.repository = repository;
        this.serviceGroup = serviceGroup;
        this.serviceStudent = serviceStudent;
    }
    @Transactional
    public void save(GroupAssignmentCreationDTO dto){
        Group group = serviceGroup.findById(dto.idGroup());
        Student student = serviceStudent.findById(dto.idStudent());
        Rotation rotation = group.getRotation();
        if (repository.countByGroupId(dto.idGroup())<group.getCapacity()&&!repository.existsScheduleConflict(student, rotation.getStartDate()    , rotation.getCompletionDate())) {
            repository.save(GroupAssignment.builder()
        .id(UUIDGenerator.newId())
        .student(student)
        .group(group)
        .build()
        );
        }else{
            throw new DataIntegrityViolationException("Violation consistency from Group assignment and Group");
        }
    }
    @Transactional
    public void delete(String id){
        GroupAssignment groupAssignment = repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        repository.delete(groupAssignment);
    }
    public GroupAssignment findById(String id){
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
    }
    
    public Page<GroupAssignment> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }
    @Transactional
    public void update(String id, GroupAssignmentCreationDTO dto){
        GroupAssignment groupAssignment = repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        Group group = serviceGroup.findById(dto.idGroup());
        Student student = serviceStudent.findById(dto.idStudent());
        Rotation rotation = group.getRotation();
        if (repository.countByGroupId(dto.idGroup())<group.getCapacity()&&repository.existsScheduleConflict(student, rotation.getStartDate()    , rotation.getCompletionDate())) {
            groupAssignment.setGroup(group);
            groupAssignment.setStudent(student);
            repository.save(groupAssignment);
        }else{
            throw new DataIntegrityViolationException("Violation consistency");
        }

    }
}
