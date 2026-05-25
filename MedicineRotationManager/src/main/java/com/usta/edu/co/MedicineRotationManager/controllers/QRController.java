package com.usta.edu.co.MedicineRotationManager.controllers;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.usta.edu.co.MedicineRotationManager.models.EntryPractice;
import com.usta.edu.co.MedicineRotationManager.services.QRService;
import com.usta.edu.co.MedicineRotationManager.services.ServiceEntryPractice;


@RestController
@RequestMapping("/qr")
public class QRController {

    private final QRService qrService;
    private final ServiceEntryPractice serviceEntryPractice;


    public QRController(QRService qrService, ServiceEntryPractice serviceEntryPractice) {
        this.qrService = qrService;
        this.serviceEntryPractice = serviceEntryPractice;
    }


    @GetMapping("/generate/{id}")
    public ResponseEntity<byte[]> generateQR(
            @PathVariable String id)
            throws WriterException, IOException {
                EntryPractice entryPractice = serviceEntryPractice.findById(id);

        byte[] qrImage = qrService.generateQR(entryPractice.getQrCode(), 300, 300);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }
}

