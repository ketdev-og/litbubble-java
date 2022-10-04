package com.bitbubble.api.app.util.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bitbubble.api.app.service.EmailMailSenderService;
import com.bitbubble.api.app.util.events.SendEmailEvent;

import io.jsonwebtoken.io.IOException;

@Component
public class SendEmailListner {

    @Autowired
    private EmailMailSenderService emailMailSenderService;

    @Async
    @EventListener
    public void sendEmailAfterReg(SendEmailEvent event) throws IOException, MessagingException{
         SendEmailEvent mailinfo = new SendEmailEvent();
         mailinfo.setEmailAddress(event.getEmailAddress());
         mailinfo.setEmailSubject(event.getEmailSubject());

         Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", event.getEmailAddress());
        model.put("verifyCode", event.getVerifyCode());
       
        mailinfo.setProps(model);
            
        emailMailSenderService.sendEmail(mailinfo);
    }
}
 