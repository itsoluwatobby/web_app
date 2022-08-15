package com.example.thymeleaf.template;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String hello(){
        return "hello";
    }
}
