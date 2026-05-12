package com.usta.edu.co.MedicineRotationManager.handlers;

import org.springframework.web.bind.annotation.RestController;

import com.usta.edu.co.MedicineRotationManager.services.MailManager;

import jakarta.mail.MessagingException;

import org.aspectj.weaver.ast.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/test")
public class test {
   private MailManager mailManager;
 public test(MailManager mailManager) {
      this.mailManager = mailManager;
   }
 @GetMapping("test1/{word}")
 public String testGet(@PathVariable String x, @RequestParam(required = false, defaultValue = "gurruple") String y){
    return "test"+x+y;
 }
 @PostMapping("test2/post")
 public String test() {
    try {
      mailManager.sendMessage("wissencsi@gmail.com", "test");
      return "hola";
    } catch (MessagingException e) {
     return "p";
    }

 }
}
