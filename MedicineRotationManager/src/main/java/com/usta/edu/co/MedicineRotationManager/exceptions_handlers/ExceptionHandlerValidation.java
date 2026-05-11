package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.usta.edu.co.MedicineRotationManager.dtos.Err;

@RestControllerAdvice
public class ExceptionHandlerValidation {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Err> failsValid(MethodArgumentNotValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Err(400, "invalid paramthers", HttpStatus.BAD_REQUEST.name()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Err> constrainsViolation(ConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Err(400, "Constrains Violation Jakarta", HttpStatus.BAD_REQUEST.name()));
    }
}
