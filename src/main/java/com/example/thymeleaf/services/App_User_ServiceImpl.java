package com.example.thymeleaf.services;

import com.example.thymeleaf.entity.App_Users;
import com.example.thymeleaf.entity.ConfirmationToken;
import com.example.thymeleaf.entity.Custom_App_Users;
import com.example.thymeleaf.entity.PasswordResetToken;
import com.example.thymeleaf.entity.enums.AppPermission;
import com.example.thymeleaf.event.EmailSender;
import com.example.thymeleaf.model.RegistrationRequest;
import com.example.thymeleaf.repositories.AppUserRepository;
import com.example.thymeleaf.repositories.ConfirmationTokenRepository;
import com.example.thymeleaf.repositories.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.thymeleaf.entity.enums.App_User_Role.*;

@Transactional
@Service @AllArgsConstructor @Slf4j
public class App_User_ServiceImpl implements App_User_Service, UserDetailsService{

    private AppUserRepository appUserRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder encoder;
    private EmailSender validEmail;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         App_Users app_users = appUserRepository.findByEmail(email);
        if(app_users == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.info("User {} found in database", app_users.getEmail());
        }
        return new Custom_App_Users(app_users);
    }
    @Override
    public App_Users registerNewUser(RegistrationRequest modelRequest) {
        boolean valid = validEmail.validateEmail(modelRequest.getEmail());
        App_Users emailAddress = appUserRepository.findByEmail(modelRequest.getEmail());

        if(!valid) {
            log.info("Invalid email address");
            throw new IllegalStateException(String.format("%s is not a valid email address", modelRequest.getEmail()));
        }

        else if(emailAddress != null) {
            throw new IllegalStateException("User already exist");
        }

        else {
            App_Users app_users = new App_Users(modelRequest.getFirstName(), modelRequest.getLastName(),
                    modelRequest.getEmail(), encoder.encode(modelRequest.getPassword()), USER);

            appUserRepository.save(app_users);
            return app_users;
        }
    }

    @Override
    public void saveTokenWithUser(App_Users app_users, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, app_users);
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public String verifyConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if(confirmationToken == null) {
            return "invalid";
        }

            App_Users app_users = confirmationToken.getApp_users();
            Calendar calendar = Calendar.getInstance();

        long tokenExpiredTime = confirmationToken.getExpiresAt().getTime() - calendar.getTime().getTime();

            if(tokenExpiredTime <= 0) {
                confirmationTokenRepository.delete(confirmationToken);
                return "Verification Token expired";
            }

            app_users.setEnabled(true);
            appUserRepository.save(app_users);

        return "valid";
    }

    @Override
    public ConfirmationToken generateNewConfirmationToken(String oldToken) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(oldToken);
        confirmationToken.setToken(UUID.randomUUID().toString());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public App_Users findUserByEmail(String email) {
        App_Users app_users = appUserRepository.findByEmail(email);
        if (app_users == null) {
            throw new UsernameNotFoundException("User with email not found");
        }
        return app_users;
    }

    @Override
    public void createPasswordResetTokenForUser(App_Users app_users, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, app_users);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            return "invalid";
        }

        Calendar calendar = Calendar.getInstance();

        long tokenExpiredTime = passwordResetToken.getExpiresAt().getTime() - calendar.getTime().getTime();

        if(tokenExpiredTime <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "Verification Token expired";
        }

        return "valid";
    }

    @Override
    public Optional<App_Users> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getApp_users());
    }

    @Override
    public void changePassword(App_Users app_users, String newPassword) {
        app_users.setPassword(encoder.encode(newPassword));
        appUserRepository.save(app_users);
    }

    @Override
    public boolean checkIfValidOldPassword(App_Users app_users, String oldPassword) {
        return encoder.matches(oldPassword, app_users.getPassword());
    }
}
