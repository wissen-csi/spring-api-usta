package com.usta.edu.co.MedicineRotationManager.components;



import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.usta.edu.co.MedicineRotationManager.dto.MessageStudentDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.TemplateEnum;
import com.usta.edu.co.MedicineRotationManager.services.ServiceNotify;
@Component
public class NotifactionScheduler {

    private ServiceNotify serviceNotify;
    private MailManager mailManager;


    public NotifactionScheduler(ServiceNotify serviceNotify,MailManager mailManager) {
        this.serviceNotify = serviceNotify;
        this.mailManager = mailManager;
    }


    @Scheduled(cron = "*/10 * * * * *")
    public void sendNotification() throws Exception{
           Context context = new Context();
   List<MessageStudentDTO> list = new LinkedList<>();
   list.add(new MessageStudentDTO("jorge", "4546456", LocalDate.now(), true));
   list.add(new MessageStudentDTO("enano pichon", "64645", LocalDate.of(1, 1, 1), false));
   context.setVariable("students", list);
    mailManager.sendNotifyArl("danieljimenezgg@gmail.com",TemplateEnum.ARL,context,"arl");
    System.out.println("hola");
    }

}
