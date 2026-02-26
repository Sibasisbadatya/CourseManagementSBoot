package com.project.CourseManagement.service;

import com.project.CourseManagement.exception.NotificationError;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private static final String ACCOUNT_SID = "sibasis";
    private static final String AUTH_TOKEN = "sibasis";
    private static final String FROM_NUMBER = "+sibasis";
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
