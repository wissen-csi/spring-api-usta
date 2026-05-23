package com.usta.edu.co.MedicineRotationManager.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.usta.edu.co.MedicineRotationManager.components.MailManager;
import com.usta.edu.co.MedicineRotationManager.dto.MessageStudentDTO;
import com.usta.edu.co.MedicineRotationManager.enumerations.AppRole;
import com.usta.edu.co.MedicineRotationManager.enumerations.TemplateEnum;

import jakarta.mail.MessagingException;

@Service
public class ServiceNotify {
private MailManager mailManager;
private ServiceStudent serviceStudent;
private ServicePerson servicePerson;

public ServiceNotify(MailManager mailManager, ServiceStudent serviceStudent, ServicePerson servicePerson) {
    this.mailManager = mailManager;
    this.serviceStudent = serviceStudent;
    this.servicePerson = servicePerson;
}

public void sendCloseToArl() throws Exception{
    Context context = new Context();
    List<MessageStudentDTO> list= serviceStudent
    .findCloseToExpireARL()
    .stream()
    .map(x->new MessageStudentDTO(x.getName()+" "+x.getLastName(), x.getDni(), x.getArlEndDate(), !LocalDate.now().isBefore(x.getArlEndDate()))).toList();
    context.setVariable("students", list);
    List<String> emailsAdmin = servicePerson.findEmailsByRole(AppRole.ADMIN);
    emailsAdmin.forEach(x -> {
        try {
            mailManager.sendNotifyArl(x, TemplateEnum.ARL, context, "close to expire ARL");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    });
}
}