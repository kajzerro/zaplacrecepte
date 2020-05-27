package com.hastlin.zaplacrecepte.service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {
    public void sendSms(String message, String phoneNumber, String sender){
        AmazonSNS amazonSNS =  AmazonSNSClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://sns.eu-central-1.amazonaws.com/", "eu-central-1")).build();
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
        messageAttributeValue.setStringValue(sender);
        messageAttributeValue.setDataType("String");
        smsAttributes.put("AWS.SNS.SMS.SenderID", messageAttributeValue);
        sendSMSMessage(amazonSNS, message, phoneNumber, smsAttributes);
    }
    private void sendSMSMessage(AmazonSNS amazonSNS, String message,
                                      String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = amazonSNS.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
    }
}