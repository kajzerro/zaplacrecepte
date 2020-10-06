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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PrescriptionService {

    private static final String STATUS_NEW = "NEW";
    private static final String MAIL_SUBJECT = "RECEPTA";
    private static final String MAIL_REQUEST_PAYMENT_TEXT = "Dr Adam Dyrda prosi o oplacenie recepty: ";
    private static final String MAIL_WITH_PRESCRIPTION_TEXT = "Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.";

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
            sendPaymentRequestsViaEmailAndSms(prescriptionEntity);
        }
        catch (PaymentException | RestClientException e) {
            log.error("Communication with payment provider failed: {}", e.getMessage());
            prescriptionEntity.addError("P24: " + this.actualDateTime() + " " + e.getMessage());
        }
        this.prescriptionRepository.save(prescriptionEntity);
    }

    public void sendPaymentRequestsViaEmailAndSms(PrescriptionEntity prescriptionEntity) {
        sendEmailWithPaymentRequest(prescriptionEntity);
        sendSmsWithPaymentRequest(prescriptionEntity);
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


    public ZonedDateTime actualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Warsaw"));
        return zonedDateTime;
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
            if (!StringUtils.isEmpty(updateEntity.getPrescriptionNumber())) {
                sendPrescriptionNumber(updateEntity);
            } else {
                log.warn("Prescription {} changed to COMPLETED without prescription number filled", prescriptionEntity.getId());
            }
            paymentService.sendPaymentToPartner(prescriptionEntity);
        } else if (prescriptionEntity.getStatus().equals("WAITING_FOR_CONFIRMATION") && updateEntity.getStatus().equals("CANCELED")) {
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
        String prescriptionReadyMessage = String.format(MAIL_WITH_PRESCRIPTION_TEXT, prescriptionEntity.getPrescriptionNumber());
        try {
            this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, prescriptionReadyMessage);
        } catch (MessagingException | RuntimeException e) {
            log.error("Email could not be delivered: {}", e.getMessage());
            prescriptionEntity.addError("Email: " + this.actualDateTime() + " " + e.getMessage());
        }
        try {
            this.smsService.sendSms(prescriptionReadyMessage, prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        }
        catch (RuntimeException e) {
            log.error("Sending sms failed: {}", e.getMessage());
            prescriptionEntity.addError("SMS: " + this.actualDateTime() + " " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        prescriptionEntity.setPrescriptionNumber("2345");
        System.out.println(String.format(MAIL_WITH_PRESCRIPTION_TEXT, prescriptionEntity.getPrescriptionNumber()));
    }
}
