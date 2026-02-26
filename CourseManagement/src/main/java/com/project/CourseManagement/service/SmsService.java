package com.project.CourseManagement.service;

import com.project.CourseManagement.exception.NotificationError;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private static final String ACCOUNT_SID = "AC74c8f5e21021307783f7ae662543637b";
    private static final String AUTH_TOKEN = "0de64bf9e8f3ee1c27eb737d88eb5d1e";
    private static final String FROM_NUMBER = "+12706067390";
    @PostConstruct
    private void init(){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
    }

    public void sendSms(String toNumber,String message){
        try {
            Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(FROM_NUMBER),
                    message
            ).create();
        } catch (RuntimeException e) {
            throw new NotificationError(e.getMessage());
        }
    }

}
