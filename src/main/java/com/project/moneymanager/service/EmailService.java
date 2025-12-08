package com.project.moneymanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;


    public void sendEmail(String to, String subject, String body) {
        // Implementation for sending email
        try{
            SimpleMailMessage messgage = new SimpleMailMessage();
            messgage.setFrom(fromEmail);
            messgage.setTo(to);
            messgage.setSubject(subject);
            messgage.setText(body);
            mailSender.send(messgage);
        }
        catch (Exception e){
            throw new RuntimeException((e.getMessage()));
        }
    }

}
