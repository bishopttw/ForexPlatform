package com.bishop.forexplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendVerificationEmail(String toEmail, String token) {
        System.out.println("VERIFICATION LINK for " + toEmail + ": " + baseUrl + "/verify?token=" + token);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Verify your account");
            message.setText("Welcome! Please verify your account by clicking this link:\n"
                    + baseUrl + "/verify?token=" + token);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email could not be sent (using console link instead): " + e.getMessage());
        }
    }
}
