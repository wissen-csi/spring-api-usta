package com.usta.edu.co.MedicineRotationManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.annotation.RequestScope;
@Configuration
public class Cie11Config {
     @Bean
    @RequestScope
    public RestClient restClient() {
        return RestClient.builder().baseUrl("https://id.who.int/icd").defaultHeader("Accept", "application/json")
                .defaultHeader("Accept-Langugae", "es").build();
    }
}
