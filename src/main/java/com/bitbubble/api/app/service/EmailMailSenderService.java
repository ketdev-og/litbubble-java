package com.bitbubble.api.app.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.bitbubble.api.app.util.events.SendEmailEvent;

import io.jsonwebtoken.io.IOException;

@Service
public class EmailMailSenderService {
    
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(SendEmailEvent mail) throws MessagingException, IOException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getProps());
    
        final String template = "inlined-css-template";
        String html = templateEngine.process(template, context);
            helper.setFrom("lilbubble@auth.com");
            helper.setTo(mail.getEmailAddress());
            helper.setText(html,true);
            helper.setSubject(mail.getEmailSubject());
           
            emailSender.send(message);
            System.out.println("message sent successfully");
    }
}
