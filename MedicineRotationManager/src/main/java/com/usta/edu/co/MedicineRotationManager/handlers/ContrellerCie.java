package com.usta.edu.co.MedicineRotationManager.handlers;

import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dtos.DiaseaseDTO;
import com.usta.edu.co.MedicineRotationManager.services.CieService;

import java.util.List;
import java.util.Map;

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
    public List<DiaseaseDTO> search(@RequestParam String term) {
        return cieService.search(term);
    }
}