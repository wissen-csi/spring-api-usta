package com.usta.edu.co.MedicineRotationManager.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.PorterCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.PorterUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Location;
import com.usta.edu.co.MedicineRotationManager.models.Porter;
import com.usta.edu.co.MedicineRotationManager.repositories.PorterRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PorterService {
    private final PorterRepository porterRepository;
    private final ServiceLocation serviceLocation;
    private final AuthUserService userService;

@Transactional
public void save(PorterCreateDTO dto) {

    Location placeBirth = serviceLocation.findById(dto.placeBirthId());
    Location residence = serviceLocation.findById(dto.residenceAddressId());

    Porter porter = Porter.builder()
            .id(UUIDGenerator.generateNewId())

            // PERSON
            .name(dto.name())
            .lastName(dto.lastName())
            .dni(dto.dni())
            .maritalStatus(dto.maritalStatus())
            .placeBirth(placeBirth)
            .residenceAddress(residence)
            .phoneNumber(dto.phoneNumber())
            .email(dto.email())
            .typeBlood(dto.typeBlood())
            .weight(dto.weight())
            .imc(dto.imc())

            // PORTER
            .employeeCode(dto.employeeCode())
            .hireDate(dto.hireDate())
            .isActive(dto.isActive())

            .build();

    porterRepository.save(porter);
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
        userService.deleteUser(porterToDelete);
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
