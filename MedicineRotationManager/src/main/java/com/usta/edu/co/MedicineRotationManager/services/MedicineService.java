package com.usta.edu.co.MedicineRotationManager.services;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.MedicineCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.MedicineResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.MedicineUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Medicine;
import com.usta.edu.co.MedicineRotationManager.repositories.MedicineRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;

    @Transactional
    public void save(MedicineCreateDTO medicineCreateDTO) {
        Medicine medicine = Medicine.builder()
                .id(UUIDGenerator.generateNewId())
                .activeIngredient(medicineCreateDTO.activeIngredient())
                .atc(medicineCreateDTO.atc())
                .descriptionAtc(medicineCreateDTO.descriptionAtc())
                .build();
        this.medicineRepository.save(medicine);
    }

    @Transactional 
    public void update(MedicineUpdateDTO medicineUpdateDTO,String id){
        Medicine medicine = findById(id);
        medicine.setActiveIngredient(medicineUpdateDTO.activeIngredient());
        medicine.setDescriptionAtc(medicineUpdateDTO.descriptonAtc());
        this.medicineRepository.save(medicine);
    }

    @Transactional
    public void delete(String id){
        Medicine medicine = findById(id);
        this.medicineRepository.delete(medicine);
    }


    @Transactional
    public Medicine findById(String id){
        return this.medicineRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Medicina No Encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<Medicine>findAll(Pageable pageable) {
        return this.medicineRepository.findAll(pageable);

    }

    public MedicineResponseDTO convertObjectToDTO(Medicine medicine) {

        return MedicineResponseDTO.builder()
                .id(medicine.getId())
                .activeIngredient(medicine.getActiveIngredient())
                .description(medicine.getDescriptionAtc())
                .build();
    }


}
