package com.example.thymeleaf.services;

import com.example.thymeleaf.entity.App_Users;
import com.example.thymeleaf.entity.ConfirmationToken;
import com.example.thymeleaf.model.RegistrationRequest;

import java.util.Optional;

public interface App_User_Service {
    App_Users registerNewUser(RegistrationRequest modelRequest);

    void saveTokenWithUser(App_Users app_users, String token);

    String verifyConfirmationToken(String token);

    ConfirmationToken generateNewConfirmationToken(String oldToken);

    App_Users findUserByEmail(String email);

    void createPasswordResetTokenForUser(App_Users app_users, String token);

    String validatePasswordResetToken(String token);

    Optional<App_Users> getUserByPasswordResetToken(String token);

    void changePassword(App_Users app_users, String newPassword);

    boolean checkIfValidOldPassword(App_Users app_users, String oldPassword);
}
