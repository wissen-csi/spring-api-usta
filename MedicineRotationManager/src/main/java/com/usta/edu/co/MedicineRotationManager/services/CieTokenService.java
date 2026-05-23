package com.usta.edu.co.MedicineRotationManager.services;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CieTokenService {
    private RestClient restClient = RestClient.create();
    @Value("${cie11.client-id}")
    private String id;
    @Value("${cie11.client-secret}")
    private String password ;
    private String token;
    private Instant tokenExpiry;

    public String getToken() {
        if (token != null && Instant.now().isBefore(tokenExpiry)) {
            return token;
        }
        Map<String, Object> response = restClient.post().uri("https://icdaccessmanagement.who.int/connect/token")
                .header("Content-Type", "application/x-www-form-urlencoded").body("client_id=" + id
                        + "&client_secret=" + password + "&grant_type=client_credentials" + "&scope=icdapi_access")
                .retrieve().body(new ParameterizedTypeReference<>() {});
                token = (String) response.get("access_token");
                int x = ((Number) response.get("expires_in")).intValue();
                tokenExpiry = Instant.now().plusSeconds(x-60);
                return token;

    }

}
