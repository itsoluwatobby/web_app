package com.example.thymeleaf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
