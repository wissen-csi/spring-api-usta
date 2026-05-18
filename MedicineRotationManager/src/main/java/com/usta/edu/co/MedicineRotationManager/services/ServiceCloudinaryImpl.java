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
import com.usta.edu.co.MedicineRotationManager.interfaces.ICloudinaryService;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;
@Service
public class ServiceCloudinaryImpl implements ICloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, String> upload(MultipartFile multipartFile) throws IOException {

        File file =
                Converter.convertMultipartFileToFile(multipartFile);

        Map<String, String> result =
                cloudinary.uploader().upload(
                        file,
                        ObjectUtils.asMap(
                                "resource_type",
                                "auto"
                        )
                );

        Files.deleteIfExists(file.toPath());

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "id",
                result.get("public_id")
        );

        response.put(
                "secure_url",
                result.get("secure_url")
        );

        response.put(
                "format",
                result.get("format")
        );

        response.put(
                "resource_type",
                result.get("resource_type")
        );

        return response;
    }


    @Override
    public Map<?, ?> upload(File file) throws IOException {
        Map<String,String> resut = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        Files.deleteIfExists(file.toPath());
        Map<String,String> response = new HashMap<>();
        response.put("id",resut.get("public_id") );
        response.put("segurity_url", resut.get("secure_url"));
        return response;
    }

    @Override
    public Map<?, ?> delete(String id) throws IOException {
        Map<?,?> result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

}
