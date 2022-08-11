package com.example.thymeleaf.event;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailBody {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public String mailBody(String url) {
        return (
                "<div>" +
                        "<p>Hi</p>"+
                     "<h3>Welcome to My Awesome Web Application</h3>"+
                    "<p>Please confirm the verification token to activate your account</p><br/>"+
                    url+
                "</div>"
                );
    }

    public boolean validateEmail (String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher match = pattern.matcher(email);

        boolean matchFound = match.matches();
        return matchFound;
    }
}
