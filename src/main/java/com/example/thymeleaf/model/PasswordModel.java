package com.example.thymeleaf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordModel {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String matchingPassword;
}
