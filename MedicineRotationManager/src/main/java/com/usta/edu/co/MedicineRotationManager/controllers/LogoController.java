package com.usta.edu.co.MedicineRotationManager.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoController {

    @GetMapping("/api/logo")
    public ResponseEntity<byte[]> getLogo() {
        try {
            ClassPathResource resource = new ClassPathResource("static/logo.png");
            byte[] imageData = resource.getInputStream().readAllBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, must-revalidate")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
