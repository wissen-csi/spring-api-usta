package com.usta.edu.co.MedicineRotationManager.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.usta.edu.co.MedicineRotationManager.interfaces.IServicioClaudinaty;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;
@Service
public class ServiceCloudinaryImpl implements IServicioClaudinaty {
    @Autowired
    private Cloudinary cloudinary;
    @Override
    public Map<?, ?> uplaud(MultipartFile multipartFile) throws IOException {
        File file = Converter.convertMultipartFileToFile(multipartFile);
        Map<String,String> resut = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        Files.deleteIfExists(file.toPath());
        Map<String,String> reponse = new HashMap<>();
        reponse.put("id",resut.get("public_id") );
        reponse.put("segurity_url", resut.get("secure_url"));
        return reponse;
        
    }

    @Override
    public Map<?, ?> uplaud(File file) throws IOException {
        Map<String,String> resut = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        Files.deleteIfExists(file.toPath());
        Map<String,String> reponse = new HashMap<>();
        reponse.put("id",resut.get("public_id") );
        reponse.put("segurity_url", resut.get("secure_url"));
        return reponse;
    }

    @Override
    public Map<?, ?> delete(String id) throws IOException {
        Map<?,?> result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

}
