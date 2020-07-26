package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.service.exception.PaymentException;
import com.hastlin.zaplacrecepte.service.p24.Payment;
import com.hastlin.zaplacrecepte.service.p24.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class PrescriptionService {

    private static final String STATUS_NEW = "NEW";
    private static final String MAIL_SUBJECT = "RECEPTA";
    private static final String MAIL_REQUEST_PAYMENT_TEXT = "Dr Marek Krzystyniak prosi o oplacenie recepty: ";
    private static final String MAIL_WITH_PRESCRIPTION_TEXT = "Numer recepty to: ";

    @Value("${p24.shortPaymentLink}")
    private String shortPaymentLink;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void createNewPrescription(PrescriptionEntity prescriptionEntity) {
        prescriptionEntity.setStatus(STATUS_NEW);
        prescriptionEntity.setCreateDateTime(actualDateTime());
        this.prescriptionRepository.save(prescriptionEntity);
        try {
            Payment payment = paymentService.createPayment(prescriptionEntity.getEmail());
            prescriptionEntity.setOrderUrl(payment.getOrderUrl());
            prescriptionEntity.setPaymentToken(payment.getPaymentToken());
            sendEmailWithPaymentRequest(prescriptionEntity);
            sendSmsWithPaymentRequest(prescriptionEntity);
        }
        catch (PaymentException | RestClientException e) {
            log.error("Communication with payment provider failed: {}", e.getMessage());
            prescriptionEntity.addError("P24: " + this.actualDateTime() + " " + e.getMessage());
        }
        this.prescriptionRepository.save(prescriptionEntity);
    }

    private void sendSmsWithPaymentRequest(PrescriptionEntity prescriptionEntity) {
        try {
            this.smsService.sendSms(MAIL_REQUEST_PAYMENT_TEXT + this.shortPaymentLink + prescriptionEntity.getId(), prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        }
        catch (RuntimeException e) {
            log.error("Sending sms failed: {}", e.getMessage());
            prescriptionEntity.addError("SMS: " + this.actualDateTime()+ " " + e.getMessage());
        }
    }

    private void sendEmailWithPaymentRequest(PrescriptionEntity prescriptionEntity) {
        try {
            this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, MAIL_REQUEST_PAYMENT_TEXT + this.shortPaymentLink + prescriptionEntity.getId());
        } catch (MessagingException | RuntimeException e) {
            log.error("Email could not be delivered: {}", e.getMessage());
            prescriptionEntity.addError("Email: " + this.actualDateTime() + " " + e.getMessage());
        }
    }


    public String actualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Warsaw"));
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public PrescriptionEntity getPrescription(String id) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findById(id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }
        return optionalPrescriptionEntity.get();
    }

    public Iterable<PrescriptionEntity> getAllPrescriptions() {
        return this.prescriptionRepository.findAll();
    }

    public void updatePrescription(String id, PrescriptionEntity updateEntity) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findById(id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }


        PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
        if (updateEntity.getStatus().equals("COMPLETED")) {
            sendPrescriptionNumber(updateEntity);
        } else if (updateEntity.getStatus().equals("CANCELED")) {
            paymentService.cancelPayment(prescriptionEntity);
        }
        prescriptionEntity.setFirstName(updateEntity.getFirstName());
        prescriptionEntity.setLastName(updateEntity.getLastName());
        prescriptionEntity.setPesel(updateEntity.getPesel());
        prescriptionEntity.setPostalCode(updateEntity.getPostalCode());
        prescriptionEntity.setRemarks(updateEntity.getRemarks());
        prescriptionEntity.setPhoneNumber(updateEntity.getPhoneNumber());
        prescriptionEntity.setEmail(updateEntity.getEmail());
        prescriptionEntity.setStatus(updateEntity.getStatus());
        prescriptionEntity.setPrescriptionNumber(updateEntity.getPrescriptionNumber());
        prescriptionRepository.save(prescriptionEntity);
    }

    private void sendPrescriptionNumber(PrescriptionEntity prescriptionEntity) {
        try {
            this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, MAIL_WITH_PRESCRIPTION_TEXT + prescriptionEntity.getPrescriptionNumber());
        } catch (MessagingException | RuntimeException e) {
            log.error("Email could not be delivered: {}", e.getMessage());
            prescriptionEntity.addError("Email: " + this.actualDateTime() + " " + e.getMessage());
        }
        try {
            this.smsService.sendSms(MAIL_WITH_PRESCRIPTION_TEXT + prescriptionEntity.getPrescriptionNumber(), prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        }
        catch (RuntimeException e) {
            log.error("Sending sms failed: {}", e.getMessage());
            prescriptionEntity.addError("SMS: " + this.actualDateTime() + " " + e.getMessage());
        }
    }
}
