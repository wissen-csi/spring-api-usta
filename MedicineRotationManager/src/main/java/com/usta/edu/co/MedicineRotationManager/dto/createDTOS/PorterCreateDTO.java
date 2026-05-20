package com.usta.edu.co.MedicineRotationManager.dto.createDTOS;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.repositories.PorterRepository;

import lombok.RequiredArgsConstructor;

public record PorterCreateDTO(LocalDate hireDate, String employeeCode, boolean isActive) {

}
