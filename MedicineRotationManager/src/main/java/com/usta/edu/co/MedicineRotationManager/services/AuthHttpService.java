package com.usta.edu.co.MedicineRotationManager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.LoginResponseDTO;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthHttpService {

        private String jwtToken;

        public void login(String dni, String password) {

                String json = String.format("""
                                {
                                     "dni": "%s",
                                     "password": "%s"
                                 }
                                 """,
                                dni,
                                password);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/auth/login"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();

                HttpClient client = HttpClient.newHttpClient();

                try {

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        ObjectMapper mapper = new ObjectMapper();
                        LoginResponseDTO loginResponse = mapper.readValue(response.body(), LoginResponseDTO.class);
                        jwtToken = loginResponse.getToken();
                        System.out.println(jwtToken);

                } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                }
        }

        public String getJwtToken() {
                return jwtToken;
        }
}