package com.usta.edu.co.MedicineRotationManager.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.usta.edu.co.MedicineRotationManager.components.MailManager;
import com.usta.edu.co.MedicineRotationManager.dto.MessageStudentDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.TemplateEnum;

import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
 public void test() throws MessagingException {
   Context context = new Context();
   List<MessageStudentDTO> list = new LinkedList<>();
   list.add(new MessageStudentDTO("jorge", "4546456", LocalDate.now(), true));
   list.add(new MessageStudentDTO("enano pichon", "64645", LocalDate.of(1, 1, 1), false));
   context.setVariable("students", list);
    mailManager.sendNotifyArl("wissencsi@gmail.com",TemplateEnum.ARL,context,"arl");
    

 }
}
