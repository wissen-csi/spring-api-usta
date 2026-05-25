package com.usta.edu.co.MedicineRotationManager.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;

public class Converter {

    public static File convertMultipartFileToFile(@NonNull MultipartFile multipartFile) throws IOException {
        File file = Files.createTempFile("excel-", ".xlsx").toFile();
        multipartFile.transferTo(file);
        return file;
    }

    public static String convertHTMLToString(@NonNull String html){
        return Jsoup.parse(html).text();
    }
    public static String convertURI(String uri){
        return uri.substring(uri.indexOf("/release")); 
    }
}
