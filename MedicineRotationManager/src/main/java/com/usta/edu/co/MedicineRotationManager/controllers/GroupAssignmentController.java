package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.GroupAssignmentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.GroupAssignmentResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.AuthUser;
import com.usta.edu.co.MedicineRotationManager.models.GroupAssignment;
import com.usta.edu.co.MedicineRotationManager.services.ServiceGroupAssignment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("group/assignment")
public class GroupAssignmentController {
    private ServiceGroupAssignment serviceGroupAssignment;

    public GroupAssignmentController(ServiceGroupAssignment serviceGroupAssignment) {
        this.serviceGroupAssignment = serviceGroupAssignment;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> create(@RequestBody GroupAssignmentCreateDTO dto){
        serviceGroupAssignment.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")

    public ResponseEntity<Void> delete(@PathVariable String id){
        serviceGroupAssignment.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")

    public ResponseEntity<Page<GroupAssignmentResponseDTO>> findAll(Pageable pageable){
        Page<GroupAssignment> page = serviceGroupAssignment.findAll(pageable);
        Page<GroupAssignmentResponseDTO> response = page.map(x-> new GroupAssignmentResponseDTO(x.getId(),x.getStudent().getId(),x.getGroup().getId()));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<GroupAssignmentResponseDTO> findById(@PathVariable String id){
        GroupAssignment groupAssignment = serviceGroupAssignment.findById(id);
        return ResponseEntity.ok(new GroupAssignmentResponseDTO(groupAssignment.getId(),groupAssignment.getStudent().getId(),groupAssignment.getGroup().getId()));
    }
    @GetMapping("/find/all/self")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<GroupAssignmentResponseDTO>> findAll(@AuthenticationPrincipal AuthUser user, Pageable pageable){
        Page<GroupAssignment> page = serviceGroupAssignment.findAll(user.getId(),pageable);
        Page<GroupAssignmentResponseDTO> response = page.map(x-> new GroupAssignmentResponseDTO(x.getId(),x.getStudent().getId(),x.getGroup().getId()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody GroupAssignmentCreateDTO dto){
        serviceGroupAssignment.update(id, dto);
        return  ResponseEntity.noContent().build();
    }

}
