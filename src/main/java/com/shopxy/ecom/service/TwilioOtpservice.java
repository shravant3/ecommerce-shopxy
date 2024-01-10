package com.shopxy.ecom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioOtpservice {
    
    @Value("${twilio.account.sid}")
    private String accountsid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void sendOtp(String to,String otp){
        Twilio.init(accountsid,authToken);
        Message message=Message.creator(new PhoneNumber(to), new PhoneNumber(twilioPhoneNumber),"your OTP is :"+otp).create();
        System.out.println("SID"+message.getSid());
       
    }
}
