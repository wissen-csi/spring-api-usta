package com.usta.edu.co.MedicineRotationManager.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IServicioClaudinaty {
    public Map<?,?> uplaud(MultipartFile multipartFile) throws IOException;
    public Map<?,?> uplaud(File file) throws IOException;
    public Map<?,?> delete(String id) throws IOException;
}
