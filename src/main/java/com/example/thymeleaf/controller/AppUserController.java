package com.example.thymeleaf.controller;

import com.example.thymeleaf.entity.App_Users;
import com.example.thymeleaf.entity.ConfirmationToken;
import com.example.thymeleaf.event.EmailSender;
import com.example.thymeleaf.event.MailBody;
import com.example.thymeleaf.event.RegistrationCompleteEvent;
import com.example.thymeleaf.model.PasswordModel;
import com.example.thymeleaf.model.RegistrationRequest;
import com.example.thymeleaf.services.App_User_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AppUserController {

    private EmailSender emailSender;
    private MailBody sendBody;
    private App_User_Service app_user_service;
    private ApplicationEventPublisher publisher;

    @PostMapping("/registration")
    public String registerUser(@RequestBody RegistrationRequest modelRequest, final HttpServletRequest request) {
        App_Users app_users = app_user_service.registerNewUser(modelRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(
                app_users,
                applicationUrl(request)
        ));

        return "Confirmation link sent";
    }

    @GetMapping("/verifyRegistration")
    public String verifyToken(@RequestParam("token") String token) {
        String result = app_user_service.verifyConfirmationToken(token);

        if(result.equalsIgnoreCase("valid")) {
            return "Verification successful";
        }
        else{
            return "Invalid token";
        }
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        ConfirmationToken confirmationToken = app_user_service.generateNewConfirmationToken(oldToken);
        //resend token;
        App_Users app_user = confirmationToken.getApp_users();
        resendVerificationTokenMail(app_user, applicationUrl(request), confirmationToken);
        return "verification link sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request) {
        App_Users app_users = app_user_service.findUserByEmail(passwordModel.getEmail());

        String url = "";
        if(app_users != null) {
            String token = UUID.randomUUID().toString();
            app_user_service.createPasswordResetTokenForUser(app_users, token);
            url = passwordResetTokenMail(app_users, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/saveNewPassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordmodel) {
        String result = app_user_service.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return "expired token";
        }
        Optional<App_Users> app_users = app_user_service.getUserByPasswordResetToken(token);
        if(app_users.isPresent()) {
            app_user_service.changePassword(app_users.get(), passwordmodel.getNewPassword());
            return "Password reset successful";
        }
        else{
            return "Invalid token";
        }
    }

    @PostMapping("/changePassword")
    public String changeUserPassword(@RequestBody PasswordModel passwordModel) {
        App_Users app_users = app_user_service.findUserByEmail(passwordModel.getEmail());
        if(!app_user_service.checkIfValidOldPassword(app_users, passwordModel.getOldPassword())){
            return "invalid password";
        }
        app_user_service.changePassword(app_users, passwordModel.getNewPassword());
        return "Password changed successfully";
    }

    private String passwordResetTokenMail(App_Users app_users, String applicationUrl, String token) {
        String url = applicationUrl+"/saveNewPassword?token="+token;

        emailSender.sendMailTo(
                app_users.getEmail(), "Open To Verify Account", sendBody.mailBody(url));

        log.info("Click the link provided to reset your password: "+ url);
        log.info("Mail sent successfully");
        return url;
    }

    private void resendVerificationTokenMail(App_Users app_user, String applicationUrl, ConfirmationToken confirmationToken) {
        String url = applicationUrl+"/resendVerifyToken?token="+ confirmationToken.getToken();

        emailSender.sendMailTo(
                app_user.getEmail(), "Open To Verify Account", sendBody.mailBody(url));

        log.info("Click the link provided to verify your account: "+ url);
        log.info("Mail sent successfully");
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() +":"+ request.getServerPort()+request.getContextPath();
    }
}
