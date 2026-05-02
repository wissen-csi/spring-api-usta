package com.usta.edu.co.MedicineRotationManager.handlers;

import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.services.CieService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("api/test")
public class ContrellerCie {

    private final CieService cieService;

    public ContrellerCie(CieService cieService) {
        this.cieService = cieService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String term) {
        return ResponseEntity.ok(cieService.search(term));
    }
}