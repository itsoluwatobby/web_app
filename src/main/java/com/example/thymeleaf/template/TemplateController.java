package com.example.thymeleaf.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
