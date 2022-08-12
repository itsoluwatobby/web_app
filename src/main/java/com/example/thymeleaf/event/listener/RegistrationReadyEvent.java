package com.example.thymeleaf.event.listener;

import com.example.thymeleaf.entity.App_Users;
import com.example.thymeleaf.event.EmailSender;
import com.example.thymeleaf.event.RegistrationCompleteEvent;
import com.example.thymeleaf.services.App_User_Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class RegistrationReadyEvent implements ApplicationListener<RegistrationCompleteEvent> {

    private App_User_Service app_user_service;
    private EmailSender emailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create the verification token for the user with link
        App_Users app_users = event.getApp_users();
        String token = UUID.randomUUID().toString();

        app_user_service.saveTokenWithUser(app_users, token);
        //send mail to user
        String url = event.getApplicationUrl()+"/verifyRegistration?token="+token;

        emailSender.sendMailTo(
                app_users.getEmail(), "Open To Verify Account", emailSender.mailBody(url));

        log.info("Click the link provided to verify your account: "+ url);
        log.info("Mail sent successfully");
    }
}
