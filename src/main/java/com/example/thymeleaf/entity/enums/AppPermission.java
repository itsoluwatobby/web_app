package com.example.thymeleaf.entity.enums;

public enum AppPermission {

    APPLICATION_READ("application:read"),
    APPLICATION_WRITE("application:write");

    private final String permissions;

    AppPermission(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }
}
