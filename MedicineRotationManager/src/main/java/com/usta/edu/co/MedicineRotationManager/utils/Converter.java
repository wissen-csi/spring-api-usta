package com.usta.edu.co.MedicineRotationManager.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Converter {

    public static File converMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try(FileOutputStream stream = new FileOutputStream(file)){
            stream.write(multipartFile.getBytes());
        }
        return file;
    }
}
