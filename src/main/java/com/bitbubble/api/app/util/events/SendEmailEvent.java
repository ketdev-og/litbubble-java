package com.bitbubble.api.app.util.events;

import java.util.Map;

import lombok.Data;

@Data
public class SendEmailEvent {
    private String emailAddress;
    private String emailSubject;
    private Map<String, Object> props;
    public String verifyCode;
    public String resetToken;

}
