package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCieDTO;
import com.usta.edu.co.MedicineRotationManager.dto.createDTOS.DiseaseCreateDTO;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;

import tools.jackson.databind.JsonNode;

@Service
public class CieService {

    private final RestClient restClient;
    private final CieTokenService cieTokenService;

    public CieService(RestClient restClient, CieTokenService cieTokenService) {
        this.restClient = restClient;
        this.cieTokenService = cieTokenService;
    }

    public List<DiseaseCieDTO> searchDiaseases(String word) {
                Map<String,Object> response = restClient.get()
                .uri("/release/11/2024-01/mms/search?q={q}", word)
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
                return((List<Map<String,Object>>)response.get("destinationEntities")).stream()
                .filter(x-> Boolean.TRUE.equals(x.get("isLeaf")))
                .filter(x -> x.get("theCode")!= null)
                .filter(x -> x.get("id") != null)  
                .filter(x -> !((String)x.get("id")).contains(" "))

                .sorted((a, b) -> Double.compare(
                        ((Number) b.get("score")).doubleValue(), ((Number) a.get("score")).doubleValue()))
                .map(x-> new DiseaseCieDTO((String) x.get("id"), String.valueOf(x.get("theCode")), Converter.convertHTMLToString((String) x.get("title")))).limit(10).toList();

    }
    
    public DiseaseCreateDTO searchSpecificDiaseasse(DiseaseCieDTO diseaseCieDTO){
                JsonNode response=  restClient.get()
                .uri(Converter.convertURI(diseaseCieDTO.fundationURI()))
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(JsonNode.class);
                return new DiseaseCreateDTO(response.get("@id").asString(),
                 response.path("code").asString(), response.path("definition").path("@value").asString(), response.path("title").path("@value").asString());

    }
    @Deprecated
    public Map<String,Object> searchSpecific(String id) {
         return  restClient.get()
                .uri("/entity/{id}", id)
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
        
    }
}
