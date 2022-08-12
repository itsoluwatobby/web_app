package com.example.thymeleaf.config;

import com.example.thymeleaf.entity.enums.AppPermission;
import com.example.thymeleaf.entity.enums.App_User_Role;
import com.example.thymeleaf.services.App_User_ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.thymeleaf.entity.enums.AppPermission.*;
import static com.example.thymeleaf.entity.enums.App_User_Role.USER;

@Configuration @EnableWebSecurity
public class WebConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] WHITE_LISTED_URLS = {"/", "/js/**", "/css/**", "/registration",
            "/verifyRegistration", "/resendVerifyToken", "/resetPassword", "/saveNewPassword", "/changePassword"};

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LISTED_URLS).permitAll()
                .antMatchers("/hello").hasRole(USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }


    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }
}
