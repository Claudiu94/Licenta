package com.mainPackage.util;

import com.mainPackage.Controlers.RestControllerServices;
import com.mainPackage.database.ConnectionToDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Created by claudiu on 14.05.2017.
 */
public class NotificationCron {
    @Autowired
    ConnectionToDB connectionToDB;
    @Autowired
    StocksBrief stocksBrief;

    private static String USER_NAME = "financeanalyzer2017";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "oparola123456789!@#$%^&*("; // GMail password

    @Scheduled(cron = "*/20 * * * * ?")
    public void demoServiceMethod() {
        System.out.println("Method executed at every 20 seconds. Current time is :: " + new Date());
        checkNotifications();

    }

    public void checkNotifications() {
        List<Map<String, Object>> data = connectionToDB.getAllAlerts();

        for (Map<String, Object> map : data) {
            int id = (Integer)map.get("PersonId");
            String email = connectionToDB.getEmail(id);

            if (email != null) {
                String price = (String)map.get("Price");
                int type = (Integer)map.get("Type");
                String symbol = (String)map.get("Symbol");
                float currentPrice = stocksBrief.retreivePriceForSymbol(symbol);

                if (currentPrice != -1
                    && (type == 1 && Float.valueOf(price) < currentPrice
                        || type == -1 && Float.valueOf(price) > currentPrice)) {

                    String message = type == 1 ? "bigger" : "smaller";
                    String company = stocksBrief.retreiveNameForSymbol(symbol);
                    String currency = stocksBrief.retreiveCurrencyForSymbol(symbol);
                    sendMail(message, company, price, currency, String.valueOf(currentPrice));

                    if ((Integer)map.get("App") == 0)
                        connectionToDB.deleteNotification(id, symbol);
                    else
                        connectionToDB.emailSent(id, symbol);
                }
            }
        }
    }

    public void sendMail(String mesg, String company, String price,
                         String currency, String currentPrice) {
        // Sender's email ID needs to be mentioned
        String from = USER_NAME + "@gmail.com";
        // Recipient's email ID needs to be mentioned.
        String to = "claudiu.zafiu@yahoo.com";

        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", PASSWORD);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_NAME, PASSWORD);
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Notification[Financial Markets Real Time Analyzer]");

            // Now set the actual message
            message.setText("The price for " + company + " is " + mesg + " than " + price + " " + currency
                            + ". Current price is: " + currentPrice + " " + currency + ".");

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}