package com.usta.edu.co.MedicineRotationManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicineRotationManagerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MedicineRotationManagerApplication.class, args);
	}

}
