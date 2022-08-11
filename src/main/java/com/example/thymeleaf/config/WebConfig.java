package com.example.thymeleaf.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder encoder;
    private UserDetailsService userDetailsService;
    private static final String[] WHITE_LISTED_URLS = {"/" ,"/js/", "/css/", "/registration", "/verifyRegistration",
            "/hello", "/resendVerifyToken", "/resetPassword", "/saveNewPassword", "/changePassword"};


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .authorizeHttpRequests()
                .antMatchers(WHITE_LISTED_URLS).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
//        return ((web) -> web.ignoring().antMatchers("images", "*/webjars/**"));
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
}
