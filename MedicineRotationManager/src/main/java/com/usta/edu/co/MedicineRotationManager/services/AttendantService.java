package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.AttendantCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.AttendantUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Attendant;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.AttendantRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendantService {

        private final AttendantRepository attendantRepository;
        private final ServiceStudent serviceStudent;

        @Transactional(readOnly = true)
        public Attendant findById(String id) {

                return this.attendantRepository.findById(id)

                                .orElseThrow(() ->

                                new EntityNotFoundException(
                                                "Attendant not found with id: " + id));
        }

        @Transactional
        public void saveAttendant(AttendantCreateDTO dto) {

                Student student = serviceStudent.findById(
                                dto.studentId());

                boolean duplicatedDni = attendantRepository
                                .existsByDni(dto.dni());

                if (duplicatedDni) {

                        throw new IllegalArgumentException(
                                        "Attendant already exists with dni: "
                                                        + dto.dni());
                }

                Attendant attendant = Attendant.builder()

                                .id(UUIDGenerator.generateNewId())

                                .name(dto.name())

                                .lastName(dto.lastName())

                                .dni(dto.dni())

                                .phoneNumber(dto.phoneNumber())

                                .typeRelative(dto.typeAttendant())

                                .student(student)

                                .build();

                this.attendantRepository.save(
                                attendant);
        }

        @Transactional
        public void delete(String id) {

                Attendant attendantToDelete = findById(id);

                this.attendantRepository.delete(attendantToDelete);
        }

        @Transactional
        public void update(AttendantUpdateDTO dto, String id) {

                Attendant attendant = findById(id);

                boolean duplicatedDni = attendantRepository
                                .existsByDniAndIdNot(
                                                dto.dni(),
                                                id);
                if (duplicatedDni) {
                        throw new IllegalArgumentException(
                                        "Another attendant already uses that dni");
                }

                attendant.setDni(dto.dni());

                attendant.setName(dto.name());

                attendant.setLastName(dto.lastName());

                attendant.setPhoneNumber(dto.phoneNumber());

                attendant.setTypeRelative(
                                dto.typeAttendant());

                this.attendantRepository.save(
                                attendant);
        }

        @Transactional(readOnly = true)
        public Page<Attendant> findAllAttendandts(Pageable pageable) {
                return this.attendantRepository.findAll(pageable);
        }

        @Transactional(readOnly = true)
        public List<Attendant> findByStudent(
                        String studentId) {

                Student student = serviceStudent.findById(
                                studentId);

                return attendantRepository
                                .findByStudent(student);
        }

        @Transactional(readOnly = true)
        public Attendant findByDni(String dni) {

                return attendantRepository
                                .findByDni(dni)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Attendant not found with dni: " + dni));
        }
}
