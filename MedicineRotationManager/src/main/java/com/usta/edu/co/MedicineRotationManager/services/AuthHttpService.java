package com.usta.edu.co.MedicineRotationManager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usta.edu.co.MedicineRotationManager.dto.LoginResponseDTO;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*Servicio especializado en autenticación HTTP */

public class AuthHttpService {
        /* Variable Encargada de almacenar el token(jwt recibido desde spring) :) */
        private String jwtToken;

        /* Metodo login que recibe la logica proveniente del controlador :3 */
        public void login(String dni, String password) {
                /*
                 * Construye un JSON a partir de los datos del usuario(convertir variables de
                 * java a formato JSON)
                 */
                String json = String.format("""
                                {
                                     "dni": "%s",
                                     "password": "%s"
                                 }
                                 """,
                                dni,
                                password);
                /* Creamos una solicitud HTTP */
                /*
                 * Por que Builder?,porque una request tiene muchas configuraciones
                 * URL,METODOS,HEADER,BODY,TIMEOUT
                 */
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/auth/login"))
                                .header("Content-Type", "application/json")
                                /*
                                 * Informacion real trasportada y definimos Metodo HTTP(POST)
                                 * ¿Por que POST?porque estamos enviando datos
                                 */
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                // Finaliza la construccion del request
                                .build();
                // Crea el Cliente HTTP(Abre la conexion con el servidor)
                HttpClient client = HttpClient.newHttpClient();

                try {
                        // Aqui se envia la request real y devuelve una respuesta HTTP
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        // Deserializador de JSONS
                        ObjectMapper mapper = new ObjectMapper();
                        // Convertimos la respuesta la cual es un JSON
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