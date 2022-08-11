package com.example.thymeleaf.event;


import com.example.thymeleaf.entity.App_Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private final App_Users app_users;
    private final String applicationUrl;

    public RegistrationCompleteEvent(App_Users app_users, String applicationUrl) {
        super(app_users);
        this.app_users = app_users;
        this.applicationUrl = applicationUrl;
    }
}
