package com.usta.edu.co.MedicineRotationManager.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;

public class Converter {

    public static File convertMultipartFileToFile(@NonNull MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try(FileOutputStream stream = new FileOutputStream(file)){
            stream.write(multipartFile.getBytes());
        }
        return file;
    }
    public static String convertHTMLToString(@NonNull String html){
        return Jsoup.parse(html).text();
    }
}
