package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.service.payu.Payment;
import com.hastlin.zaplacrecepte.service.payu.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PrescriptionService {

    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_UNPAID = "unpaid";
    private static final String MAIL_SUBJECT = "RECEPTA";
    private static final String MAIL_REQUEST_PAYMENT_TEXT = "Dr Marek Krzystyniak prosi o oplacenie recepty: ";
    private static final String PAYMENT_LINK = "https://merch-prod.snd.payu.com/pay/?orderId=33XGHR24GK200524GUEST000P01&token=eyJhbGciOiJIUzI1NiJ9.eyJvcmRlcklkIjoiMzNYR0hSMjRHSzIwMDUyNEdVRVNUMDAwUDAxIiwicG9zSWQiOiIxTkJPR2V3RSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIl0sImV4cCI6MTU5MDQzMDIxMiwiaXNzIjoiUEFZVSIsImF1ZCI6ImFwaS1nYXRld2F5Iiwic3ViIjoiUGF5VSBzdWJqZWN0IiwianRpIjoiODczMjY5MTItM2YyMS00ZjhlLWFjYTgtOWIyMTk1MTY3YzE4In0.btLSu-tr16lYN6ES2kxh7Bmeg6KlqmL68Rihs6GI4Vk#/payment";
    private static final String SHORTEN_PAYMENT_LINK = "https://api.zaplacrecepte.pl/r/";
    private static final String MAIL_WITH_PRESCRIPTION_TEXT = "Numer recepty to: ";

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void createNewPrescription(PrescriptionEntity prescriptionEntity, String clientIp) {
        prescriptionEntity.setStatus(STATUS_NEW);
        prescriptionEntity.setCreateDateTime(actualDateTime());
        this.prescriptionRepository.save(prescriptionEntity);

        Payment payment = paymentService.createPayment(clientIp);
        prescriptionEntity.setOrderId(payment.getOrderId());
        prescriptionEntity.setPaymentToken(payment.getPaymentToken());
        prescriptionEntity.setOrderRedirectToUrl(payment.getOrderRedirectToUrl());
        prescriptionEntity.setOrderRedirectFromKey(payment.getOrderRedirectFromKey());
        try {
            this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, MAIL_REQUEST_PAYMENT_TEXT + SHORTEN_PAYMENT_LINK + payment.getOrderRedirectFromKey());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        this.smsService.sendSms(MAIL_REQUEST_PAYMENT_TEXT + SHORTEN_PAYMENT_LINK + payment.getOrderRedirectFromKey(), prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        this.prescriptionRepository.save(prescriptionEntity);
    }


    public String actualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Warsaw"));
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
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
            //paymentService.acceptPayment(prescriptionEntity.getOrderId());
            sendPrescriptionNumber(updateEntity);
        } else if (updateEntity.getStatus().equals("CANCELED")) {
            paymentService.cancelPayment(prescriptionEntity.getOrderId());
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
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        this.smsService.sendSms(MAIL_WITH_PRESCRIPTION_TEXT + prescriptionEntity.getPrescriptionNumber(), prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);

    }
}
