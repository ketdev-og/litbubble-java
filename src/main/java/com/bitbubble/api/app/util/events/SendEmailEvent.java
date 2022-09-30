package com.bitbubble.api.app.util.events;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SendEmailEvent {
    private String emailAddress;
    private String emailSubject;
    private Map<String, Object> props;

}
