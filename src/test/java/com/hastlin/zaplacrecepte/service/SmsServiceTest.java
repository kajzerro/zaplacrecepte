package com.hastlin.zaplacrecepte.service;


import org.junit.Ignore;
import org.junit.Test;

public class SmsServiceTest {

    @Test
    @Ignore("takes money to run")
    public void should_send_sms() {
        SmsService smsService = new SmsService();
        smsService.sendSms("COS TAM", "+48{put yout number here}", "KAJZI");
    }

}