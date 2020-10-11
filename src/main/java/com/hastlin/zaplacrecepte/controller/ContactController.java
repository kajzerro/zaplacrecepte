package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.ContactDto;
import com.hastlin.zaplacrecepte.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@Slf4j
public class ContactController {

    private static final String EMAIL_FOR_CONTACT = "zaplacrecepte@gmail.com";
    private static final String EMAIL_TOPIC = "Kontakt ze strony";

    @Autowired
    EmailService emailService;

    @PostMapping(path = "/api/contact")
    public ResponseEntity sendContactEmail(@RequestBody ContactDto contactDto) {
        try {
            emailService.sendSimpleMessage(EMAIL_FOR_CONTACT, EMAIL_TOPIC, contactDto.getMessage());
        } catch (MessagingException e) {
            log.error("Couldn't send contact email with message {}", contactDto.getMessage());
        }
        return ResponseEntity.ok("OK");
    }

}
