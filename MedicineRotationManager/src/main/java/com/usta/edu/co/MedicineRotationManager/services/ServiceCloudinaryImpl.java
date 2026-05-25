package com.usta.edu.co.MedicineRotationManager.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.usta.edu.co.MedicineRotationManager.interfaces.ICloudinaryService;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;
@Service
public class ServiceCloudinaryImpl implements ICloudinaryService {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
        "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "tiff", "tif"
    );

    @Autowired
    private Cloudinary cloudinary;

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot == -1 ? "" : filename.substring(dot + 1).toLowerCase();
    }

    @Override
    public Map<String, String> upload(MultipartFile multipartFile) throws IOException {

        File file = Converter.convertMultipartFileToFile(multipartFile);
        String ext = getExtension(multipartFile.getOriginalFilename());
        String resourceType = IMAGE_EXTENSIONS.contains(ext) ? "image" : "raw";

        @SuppressWarnings("unchecked")
        Map<String, String> result = cloudinary.uploader().upload(
                file,
                ObjectUtils.asMap("resource_type", resourceType)
        );

        Files.deleteIfExists(file.toPath());

        Map<String, String> response = new HashMap<>();
        response.put("id", result.get("public_id"));

        String secureUrl = result.get("secure_url");
        if (secureUrl != null && "raw".equals(resourceType)) {
            secureUrl = secureUrl.replace("/image/upload/", "/raw/upload/");
        }
        response.put("secure_url", secureUrl);
        response.put("format", result.getOrDefault("format", ext));
        response.put("resource_type", resourceType);

        return response;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> upload(File file) throws IOException {
        String ext = getExtension(file.getName());
        String resourceType = IMAGE_EXTENSIONS.contains(ext) ? "image" : "raw";
        Map<String,String> result = cloudinary.uploader().upload(
                file,
                ObjectUtils.asMap("resource_type", resourceType)
        );
        Files.deleteIfExists(file.toPath());
        Map<String,String> response = new HashMap<>();
        response.put("id", result.get("public_id"));

        String secureUrl = result.get("secure_url");
        if (secureUrl != null && "raw".equals(resourceType)) {
            secureUrl = secureUrl.replace("/image/upload/", "/raw/upload/");
        }
        response.put("segurity_url", secureUrl);
        return response;
    }

    @Override
    public Map<?, ?> delete(String id) throws IOException {
        return delete(id, "image");
    }

    @Override
    public Map<?, ?> delete(String id, String resourceType) throws IOException {
        Map<?,?> result = cloudinary.uploader().destroy(id,
                ObjectUtils.asMap("resource_type", resourceType));
        return result;
    }

}
