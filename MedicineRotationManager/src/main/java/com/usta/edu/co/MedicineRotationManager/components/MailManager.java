package com.usta.edu.co.MedicineRotationManager.components;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.usta.edu.co.MedicineRotationManager.enumerations.TemplateEnum;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;

@Component
@Getter
public class MailManager {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String sender;

    public MailManager(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendNotifyArl(String email,TemplateEnum type,Context context,String reason) throws MessagingException{
        String html =templateEngine.process(type.getName(), context);

        MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setTo(email);
        messageHelper.setFrom(sender);
        messageHelper.setSubject(reason);
        messageHelper.setText(html,true);
        javaMailSender.send(mimeMessage);        
        
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
