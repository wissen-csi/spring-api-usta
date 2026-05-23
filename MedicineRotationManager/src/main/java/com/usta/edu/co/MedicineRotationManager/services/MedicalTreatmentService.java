package com.usta.edu.co.MedicineRotationManager.services;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.MedicalTreatmentCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.MedicalTreatmentUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.MedicalTreatment;
import com.usta.edu.co.MedicineRotationManager.models.Medicine;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.MedicalTreatmentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalTreatmentService {
     private final MedicalTreatmentRepository medicalTreatmentRepository;
     private final ServiceStudent serviceStudent;
     private final MedicineService medicineService;

     @Transactional
    public void save(MedicalTreatmentCreateDTO medicalTreatmentCreateDTO){
         Student student = serviceStudent.findById(medicalTreatmentCreateDTO.studentId());
         Medicine medicine = medicineService.findById(medicalTreatmentCreateDTO.medicineId());

         MedicalTreatment medicalTreatment = MedicalTreatment.builder()
                 .id(UUIDGenerator.generateNewId())
                 .startMedication(medicalTreatmentCreateDTO.startMedication())
                 .endMedication(medicalTreatmentCreateDTO.endMedication())
                 .medicine(medicine)
                 .student(student)
                 .build();
          medicalTreatmentRepository.save(medicalTreatment);
     }

     @Transactional
     public void delete(String id){
         MedicalTreatment medicalTreatment = findById(id);
         this.medicalTreatmentRepository.delete(medicalTreatment);
     }

     @Transactional
    public void update(MedicalTreatmentUpdateDTO medicalTreatmentUpdateDTO,String id){
         MedicalTreatment medicalTreatment = findById(id);
         medicalTreatment.setStartMedication(medicalTreatmentUpdateDTO.startMedication());
         medicalTreatment.setEndMedication(medicalTreatmentUpdateDTO.endMedication());
         this.medicalTreatmentRepository.save(medicalTreatment);
     }

     @Transactional
    public MedicalTreatment findById(String id){
         return this.medicalTreatmentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Tratamiento medico no existente"));
     }

     @Transactional
    public Page<MedicalTreatment> findAll(Pageable pageble){
         return this.medicalTreatmentRepository.findAll(pageble);
     }

}
