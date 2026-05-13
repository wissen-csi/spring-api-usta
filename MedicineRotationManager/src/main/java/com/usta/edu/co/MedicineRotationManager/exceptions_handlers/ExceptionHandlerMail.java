package com.usta.edu.co.MedicineRotationManager.exceptions_handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.usta.edu.co.MedicineRotationManager.dto.Err;
@RestControllerAdvice
public class ExceptionHandlerMail {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerMail.class);

    @ExceptionHandler(MailAuthenticationException.class)
    public ResponseEntity<Err> authenticationFalled(MailAuthenticationException exception){
        LOG.warn("The mail can't access",exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Err(403, "ath invalid", HttpStatus.FORBIDDEN.name()));
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<Err> mailSendFallied(MailSendException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Err(500, "err with mail send", HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }

    @ExceptionHandler(MailPreparationException.class)
    public ResponseEntity<Err> TypeNotDefinide(MailPreparationException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Err(406, "bad type definition", HttpStatus.NOT_ACCEPTABLE.name()));
    }
    
}