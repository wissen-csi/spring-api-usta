package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.usta.edu.co.MedicineRotationManager.dtos.Err;


@RestControllerAdvice
public class ExceptionHandlerGeneric {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerGeneric.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Err> geniric(Exception exception){
        LOG.warn("generic",exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Err(500, "err", HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }
}
