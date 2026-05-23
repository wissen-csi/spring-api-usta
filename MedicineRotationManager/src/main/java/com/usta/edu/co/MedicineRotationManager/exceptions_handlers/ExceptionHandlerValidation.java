package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.usta.edu.co.MedicineRotationManager.dto.Err;

@RestControllerAdvice
public class ExceptionHandlerValidation {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerValidation.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Err> failsValid(MethodArgumentNotValidException exception) {
        LOG.warn("invalid paramethers", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Err(400, "invalid paramthers", HttpStatus.BAD_REQUEST.name()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Err> constrainsViolation(ConstraintViolationException exception) {
        LOG.warn("Constrains Violation Jakarta", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Err(400, "Constrains Violation Jakarta", HttpStatus.BAD_REQUEST.name()));
    }
}
