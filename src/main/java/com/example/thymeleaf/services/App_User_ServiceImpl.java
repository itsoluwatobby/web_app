package com.example.thymeleaf.services;

import com.example.thymeleaf.entity.App_Users;
import com.example.thymeleaf.entity.ConfirmationToken;
import com.example.thymeleaf.entity.PasswordResetToken;
import com.example.thymeleaf.entity.enums.App_User_Role;
import com.example.thymeleaf.event.MailBody;
import com.example.thymeleaf.model.RegistrationRequest;
import com.example.thymeleaf.repositories.AppUserRepository;
import com.example.thymeleaf.repositories.ConfirmationTokenRepository;
import com.example.thymeleaf.repositories.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.thymeleaf.entity.enums.App_User_Role.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class App_User_ServiceImpl implements App_User_Service, UserDetailsService {

    private AppUserRepository appUserRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder encoder;
    private MailBody validEmail;
    private App_User_Role app_user_role;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         App_Users app_users = appUserRepository.findByUsername(email);
        if(app_users == null) {
            log.error("User {} not found in database", app_users);
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.info("User {} found in database", app_users);
        }
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(app_user_role.name()));
        return new org.springframework.security.core.userdetails.User(app_users.getEmail(), app_users.getPassword(), authorities);
    }
    @Override
    public App_Users registerNewUser(RegistrationRequest modelRequest) {
        boolean testEmail = validEmail.validateEmail(modelRequest.getEmail());
        if(!testEmail) {
            throw new IllegalStateException(String.format("%s is not a valid email address", modelRequest.getEmail()));
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
        App_Users app_users = appUserRepository.findByUsername(email);
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

        App_Users app_users = passwordResetToken.getApp_users();
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
