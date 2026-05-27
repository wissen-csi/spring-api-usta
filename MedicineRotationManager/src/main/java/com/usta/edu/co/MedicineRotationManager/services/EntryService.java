package com.usta.edu.co.MedicineRotationManager.services;

import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryPracticeResponseDTO;
import com.usta.edu.co.MedicineRotationManager.dto.responseDTOS.EntryResponseDTO;
import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.EntryCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.EntryUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.StatusEntry;
import com.usta.edu.co.MedicineRotationManager.models.Entry;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.EntryRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;
    private final ServiceEntryPractice entryPracticeService;
    private final ServiceStudent serviceStudent;

    @Transactional
    public void save(EntryCreateDTO entryCreateDTO) {
        Student student = findStudentByDni(entryCreateDTO.studentId());
        EntryPractice entryPractice = findEntryPracticeById(entryCreateDTO.entryPracticeId());

        Entry entry = Entry.builder()
                .id(UUIDGenerator.generateNewId())
                .entryPractice(entryPractice)
                .student(student)

                .build();
        entryRepository.save(entry);
    }

    @Transactional
    public void saveByQrCode(String qrCode, String studentId, LocalDateTime assistance) {

        EntryPractice entryPractice = entryPracticeService.findByQrCode(qrCode);
        Student student = findStudentByDni(studentId);

        LocalDateTime start = entryPractice.getStartTime();
        LocalDateTime limit = start.plusMinutes(15);
        LocalDateTime end = entryPractice.getEndTime();

        StatusEntry statusEntry;

        if (!assistance.isBefore(start) &&
                !assistance.isAfter(limit)) {

            statusEntry = StatusEntry.DENTRO;

        } else if (assistance.isAfter(end)) {

            statusEntry = StatusEntry.FUERA;

        } else {

            statusEntry = StatusEntry.FALLIDO;
        }

        Entry entry = Entry.builder()
                .id(UUIDGenerator.generateNewId())
                .entryPractice(entryPractice)
                .student(student)
                .statusEntry(statusEntry)
                .assistance(assistance)
                .build();

        entryRepository.save(entry);
    }

    @Transactional
    public void update(EntryUpdateDTO entryUpdateDTO, String id) {
        Entry entry = findEntryById(id);
        entry.setAssistance(entryUpdateDTO.assistance());
        entry.setStatusEntry(entryUpdateDTO.statusEntry());
        this.entryRepository.save(entry);
    }

    @Transactional
    public void delete(String id) {
        Entry entry = findEntryById(id);
        this.entryRepository.delete(entry);
    }

    @Transactional
    public Entry findEntryById(String id) {
        return this.entryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Student findStudentByDni(String dni) {
        return this.serviceStudent.findByDni(dni);
    }

    @Transactional(readOnly = true)
    public Page<Entry> findAll(Pageable pageable) {
        return this.entryRepository.findAll(pageable);
    }

    @Transactional
    public EntryPractice findEntryPracticeById(String id){

        return this.entryPracticeService.findById(id);
    }

    public EntryResponseDTO convertObjectToDTO(Entry entry){
       return EntryResponseDTO.builder()
                .id(entry.getId())

                .entryPracticeResponseDTO(
                        EntryPracticeResponseDTO.builder()
                                .id(entry.getEntryPractice().getId())
                                .startTime(entry.getEntryPractice().getStartTime())
                                .endTime(entry.getEntryPractice().getEndTime())
                                .groupName(entry.getEntryPractice().getGroup().getName())
                                .groupId(entry.getEntryPractice().getGroup().getId())
                                .qrCode(entry.getEntryPractice().getQrCode())
                                .build()
                )

                .assistance(entry.getAssistance())

                .studentId(entry.getStudent().getId())

                .studentDni(entry.getStudent().getDni())

                .studentName(
                        entry.getStudent().getName()
                                + " "
                                + entry.getStudent().getLastName()
                )

                .statusEntry(entry.getStatusEntry())

                .build();
    }
}
