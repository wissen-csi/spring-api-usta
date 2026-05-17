package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.dto.AdminCreateDTO;
import com.usta.edu.co.MedicineRotationManager.dto.LoginRequestDTO;
import com.usta.edu.co.MedicineRotationManager.dto.LoginResponseDTO;
import com.usta.edu.co.MedicineRotationManager.services.AuthenticationService;
import com.usta.edu.co.MedicineRotationManager.services.ServiceAdmin;

import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private ServiceAdmin serviceAdmin;

    public AuthController(AuthenticationService authenticationService, ServiceAdmin serviceAdmin) {
        this.authenticationService = authenticationService;
        this.serviceAdmin = serviceAdmin;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate")
    public String validateStudentAccess() {
        return "Access validated successfully";
    }
    

    @PostMapping("/admins")
    public void save( @RequestBody AdminCreateDTO dto){
    serviceAdmin.save(dto);
}

}
