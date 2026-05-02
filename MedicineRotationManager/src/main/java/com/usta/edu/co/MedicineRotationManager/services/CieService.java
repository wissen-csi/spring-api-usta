package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CieService {

    private final RestClient restClient;
    private final CieTokenService cieTokenService;

    public CieService(RestClient restClient, CieTokenService cieTokenService) {
        this.restClient = restClient;
        this.cieTokenService = cieTokenService;
    }

    public String search(String word) {
        return restClient.get()
            .uri("/release/11/2024-01/mms/search?q={q}", word)
            .header("Authorization", "Bearer " + cieTokenService.getToken())
            .header("API-Version", "v2")          
            .header("Accept", "application/json") 
            .header("Accept-Language", "es")      
            .retrieve()
            .body(String.class);
    }

    public String getWithId(String id) {
        return restClient.get()
            .uri("/entity/{id}", id)
            .header("Authorization", "Bearer " + cieTokenService.getToken())
            .header("API-Version", "v2")          
            .header("Accept", "application/json") 
            .header("Accept-Language", "es")      
            .retrieve()
            .body(String.class);
    }
}
