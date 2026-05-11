package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.usta.edu.co.MedicineRotationManager.dtos.Err;

import jakarta.persistence.QueryTimeoutException;

@RestControllerAdvice
public class ExceptionHandlerDataBase {
    
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerDataBase.class);

    @ExceptionHandler({DataIntegrityViolationException.class, DuplicateKeyException.class})
    public ResponseEntity<Err> dbIntegrity(DataIntegrityViolationException exception){
        LOG.warn("Err Integrity db", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Err(409, "conflict with db", HttpStatus.CONFLICT.name()));
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<Err> dbTimeOut(QueryTimeoutException exception){
        LOG.warn("Err long response time",exception);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new Err(504, "the response late long time", HttpStatus.GATEWAY_TIMEOUT.name()));

    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Err> dbBadConnection(DataAccessException exception){
        LOG.warn("Err access db",exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Err(500, "can't access with db", HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }


}
