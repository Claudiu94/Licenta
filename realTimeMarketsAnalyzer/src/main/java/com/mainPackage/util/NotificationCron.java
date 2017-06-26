package com.mainPackage.util;

import com.mainPackage.Controlers.RestControllerServices;
import com.mainPackage.database.ConnectionToDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * Created by claudiu on 14.05.2017.
 */
public class NotificationCron {
    @Autowired
    ConnectionToDB connectionToDB;

    @Scheduled(cron="0 */10 * * * ?")
    public void demoServiceMethod()
    {
        System.out.println("Method executed at every 10 minutes. Current time is :: "+ new Date());
    }
}
