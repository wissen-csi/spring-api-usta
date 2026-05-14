package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.DiseaseCieDTO;
import com.usta.edu.co.MedicineRotationManager.dto.DiseaseDTO;
import com.usta.edu.co.MedicineRotationManager.services.CieService;

import lombok.NonNull;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("api/test")
public class ContrellerCie {

    private final CieService cieService;

    public ContrellerCie(CieService cieService) {
        this.cieService = cieService;
    }

    @GetMapping("/search/{term}")
    public List<DiseaseCieDTO> search(@PathVariable @NonNull String term) {
        return cieService.searchDiaseases(term);
    }
    @PostMapping("/search/especific")
    public DiseaseDTO especific(@RequestBody @NonNull DiseaseCieDTO diseaseCieDTO){
        return cieService.searchSpecificDiaseasse(diseaseCieDTO);
    }
    @GetMapping("/test/{term}")
    @Deprecated
    public Map<String,Object> test(@PathVariable @NonNull String term){
        return cieService.searchSpecific(term);
    }
}