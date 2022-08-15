package com.example.thymeleaf.event;

//import com.example.thymeleaf.entity.App_Users;
//import com.example.thymeleaf.model.RegistrationRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
//@Slf4j
public class EmailSender {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//    @Autowired
//    public JavaMailSender mailSender;
//
//    public void sendMailTo(String toEmail, String subject, String body) {
//
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            helper.setFrom("akintobby@gmail.com");
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(body, true);
//
//            mailSender.send(mimeMessage);
//        }
//        catch(MessagingException e) {
//            log.error("Unable to send email");
//            throw new IllegalStateException("Unable to send confirmation mail");
//        }
//    }
//
    public boolean validateEmail (String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher match = pattern.matcher(email);

        boolean matchFound = match.matches();
        return matchFound;
    }
//
//    public String mailBody(String url) {
//        App_Users app_users = new App_Users();
//
//        return (
//                "<div>" +
//                        "<center style='display:inline-block;'><h1 style='background-color:lightgrey; border-radius:3px; border:none" +
//                        "'>Welcome</h1><center>" +
//                        "<br />"+
//                        "<p>Hi "+ app_users.getFirstName() +"</p>"+
//                        "<h3>Welcome to My Awesome Web Application</h3>"+
//                        "<p>Please confirm the verification token to activate your account</p><br/>"+
//                        url+
//                        "</div>"
//        );
//    }
//
//    public String resetPassword(String url) {
//        return (
//                "<div>" +
//                        "<center style='display:inline-block;'><h1 style='background-color:lightgrey; border-radius:3px; border:none" +
//                        "'>You made a request to change your password</h1><center>" +
//                        "<br />"+
//                        "<p>Hi</p>"+
//                        "<p>Kindly tap the verification link below to change your password</p>"+
//                        "<br/>"+ url+
//                "</div>"
//        );
//    }
}
