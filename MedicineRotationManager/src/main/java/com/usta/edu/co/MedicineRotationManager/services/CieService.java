package com.usta.edu.co.MedicineRotationManager.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.usta.edu.co.MedicineRotationManager.dtos.DiaseaseDTO;
import com.usta.edu.co.MedicineRotationManager.utils.Converter;

@Service
public class CieService {

    private final RestClient restClient;
    private final CieTokenService cieTokenService;

    public CieService(RestClient restClient, CieTokenService cieTokenService) {
        this.restClient = restClient;
        this.cieTokenService = cieTokenService;
    }

    public List<DiaseaseDTO> search(String word) {
                Map<String,Object> response = restClient.get()
                .uri("/release/11/2024-01/mms/search?q={q}", word)
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
                List<Map<String,Object>> resolve = ((List<Map<String,Object>>)response.get("destinationEntities")).stream()
                .filter(x-> Boolean.TRUE.equals(x.get("isLeaf")))
                .filter(x -> x.get("theCode")!= null)
                .toList();
                return resolve.stream()
                .map(x->(List<Map<String,Object>>) x.get("matchingPVs"))
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .limit(10)
                .sorted((a, b) -> Double.compare(
                        ((Number) b.get("score")).doubleValue(), ((Number) a.get("score")).doubleValue()))
                .map(x-> new DiaseaseDTO((String)x.get("foundationUri"), String.valueOf(x.get("score")), (String) Converter.convertHTMLToString((String) x.get("label")))).toList();
                
    }

    public Map<String,Object> test(String word){
                return  restClient.get()
                .uri("/release/11/2024-01/mms/search?q={q}", word)
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }
    public List<DiaseaseDTO> getWithId(String id) {
        Map<String, Object> reponse = restClient.get()
                .uri("/entity/{id}", id)
                .header("Authorization", "Bearer " + cieTokenService.getToken())
                .header("API-Version", "v2")
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
        List<Map<String, Object>> resolve = (List<Map<String, Object>>) reponse.get("destinationEntities");
        return resolve.stream().filter(x -> (boolean) x.get("isLeaf")).filter(x -> x.get("theCode") != null)
                .sorted((a, b) -> Double.compare(
                        ((Number) a.get("scores")).doubleValue(), ((Number) b.get("scores")).doubleValue()))
                .limit(5).map(x -> new DiaseaseDTO((String) x.get("id"), (String) x.get("resolve"),
                        Converter.convertHTMLToString((String) x.get("title"))))
                .toList();

    }
}
