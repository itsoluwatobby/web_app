package com.example.thymeleaf.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.thymeleaf.entity.enums.AppPermission.APPLICATION_READ;
import static com.example.thymeleaf.entity.enums.AppPermission.APPLICATION_WRITE;
import static com.google.common.collect.Sets.newHashSet;

@AllArgsConstructor
@Getter
public enum App_User_Role {

    USER(newHashSet()),
    ADMIN(newHashSet(APPLICATION_READ, APPLICATION_WRITE));

    private final Set<AppPermission> appPermission;

    private Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions = getAppPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    } 

}
