package com.usta.edu.co.MedicineRotationManager.components;




import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.usta.edu.co.MedicineRotationManager.services.ServiceNotify;
@Component
public class NotifactionScheduler {

    private ServiceNotify serviceNotify;


    public NotifactionScheduler(ServiceNotify serviceNotify ) {
        this.serviceNotify = serviceNotify;
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void sendNotification() throws Exception{
           serviceNotify.sendCloseToArl();
    }

}
