package com.usta.edu.co.MedicineRotationManager.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.PorterCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.PorterUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Porter;
import com.usta.edu.co.MedicineRotationManager.repositories.PorterRepository;

import jakarta.persistence.Column;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PorterService {
    private final PorterRepository porterRepository;

    @Transactional
    public void save(PorterCreateDTO porterCreateDTO) {
        Porter porter = Porter.builder()
                .employeeCode(porterCreateDTO.employeeCode())
                .hireDate(porterCreateDTO.hireDate())
                .isActive(porterCreateDTO.isActive())
                .build();
    }

    @Transactional
    public void update(PorterUpdateDTO porterUpdateDTO, String id) {
        Porter porter = findById(id);
        porter.setActive(porterUpdateDTO.isActive());
        this.porterRepository.save(porter);

    }

    @Transactional
    public void delete(String id) {
        Porter porterToDelete = findById(id);
        this.porterRepository.delete(porterToDelete);
    }

    @Transactional
    public Porter findById(String id) {
        return this.porterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portero No Encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<Porter> findAll(Pageable pageable) {
        return this.porterRepository.findAll(pageable);
    }

}
