package com.usta.edu.co.MedicineRotationManager.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.poiji.bind.Poiji;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.EcxelCreateStudentDTO;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;


@Service
public class ServiceEcxel {
    private ServiceStudent serviceStudent;

    public ServiceEcxel(ServiceStudent serviceStudent) {
        this.serviceStudent = serviceStudent;
    }
@Transactional
public void createStudents(MultipartFile multipartFile) throws IOException {
    File file = Converter.convertMultipartFileToFile(multipartFile);
    List<EcxelCreateStudentDTO> list =
        Poiji.fromExcel(file, EcxelCreateStudentDTO.class);
        Files.deleteIfExists(file.toPath());
        list.forEach(x->serviceStudent.save(x));
}
}
