package com.usta.edu.co.MedicineRotationManager.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;

@Component
@Getter
public class MailManager {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public MailManager(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendMessage(String email,String message) throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setTo(email);
        messageHelper.setText(message,true);
        messageHelper.setFrom(sender);
        messageHelper.setSubject("Test");
        javaMailSender.send(mimeMessage);
    }
}
