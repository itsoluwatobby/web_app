package com.example.thymeleaf.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

public class Custom_App_Users implements UserDetails {

    private App_Users app_users;

    public Custom_App_Users(App_Users app_users){
        super();
        this.app_users = app_users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(app_users.getApp_user_role().name()));
    }

    @Override
    public String getPassword() {
        return app_users.getPassword();
    }

    @Override
    public String getUsername() {
        return app_users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return app_users.isEnabled();
    }
}
