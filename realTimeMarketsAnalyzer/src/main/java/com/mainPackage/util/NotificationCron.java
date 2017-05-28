package com.mainPackage.util;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * Created by claudiu on 14.05.2017.
 */
public class NotificationCron {
    @Scheduled(cron="*/5 * * * * ?")
    public void demoServiceMethod()
    {
        System.out.println("Method executed at every 5 seconds. Current time is :: "+ new Date());
    }
}
