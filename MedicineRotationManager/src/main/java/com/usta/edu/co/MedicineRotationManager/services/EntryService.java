package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.EntryCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.updateDTOS.EntryUpdateDTO;
import com.usta.edu.co.MedicineRotationManager.models.Entry;
import com.usta.edu.co.MedicineRotationManager.models.Student;
import com.usta.edu.co.MedicineRotationManager.repositories.EntryRepository;
import com.usta.edu.co.MedicineRotationManager.repositories.StudentRepository;
import com.usta.edu.co.MedicineRotationManager.utils.UUIDGenerator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;
    private final ServiceStudent serviceStudent;

    @Transactional
    public void save(EntryCreateDTO entryCreateDTO) {
        Student student = findStudentById(entryCreateDTO.studentId());
        Entry entry = Entry.builder()
                .id(UUIDGenerator.generateNewId())
                .student(student)
                .build();
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
    public Student findStudentById(String id) {
        return this.serviceStudent.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Entry> findAll(Pageable pageable) {
        return this.entryRepository.findAll(pageable);
    }

}
