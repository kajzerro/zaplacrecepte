package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import com.hastlin.zaplacrecepte.service.exception.PaymentException;
import com.hastlin.zaplacrecepte.service.payment.Payment;
import com.hastlin.zaplacrecepte.service.payment.bm.BMPaymentService;
import com.hastlin.zaplacrecepte.service.payment.p24.P24PaymentService;
import com.hastlin.zaplacrecepte.utils.FeatureToggleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PrescriptionService {

    private static final String STATUS_NEW = "NEW";
    private static final String MAIL_SUBJECT = "MojLekarz";
    public static final String P_24_CODE = "P24";

    @Value("${payment.prescriptionBasedShortPaymentLink}")
    private String prescriptionBasedShortPaymentLink;

    @Value("${payment.serviceBasedShortPaymentLink}")
    private String serviceBasedShortPaymentLink;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private P24PaymentService p24PaymentService;

    @Autowired
    private BMPaymentService bmPaymentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void createNewPrescription(PrescriptionEntity prescriptionEntity) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        prescriptionEntity.setOwnerId(userEntity.getId());
        prescriptionEntity.setFeeIncluded(userEntity.isFeeIncluded());
        prescriptionEntity.setStatus(STATUS_NEW);
        prescriptionEntity.setCreateDateTime(actualDateTime());
        if (prescriptionEntity.getPrice() == null || FeatureToggleUtil.isLoggedUserPrescriptionBased()) {
            prescriptionEntity.setPrice(userEntity.getDefaultPrice());
        }
        this.prescriptionRepository.save(prescriptionEntity);
        try {
            Payment payment;
            if (isP24PaymentProvider(userEntity)) {
                payment = p24PaymentService.createPayment(prescriptionEntity.getEmail(), prescriptionEntity.getPrice());
            } else {
                payment = bmPaymentService.createPayment(prescriptionEntity.getPrice(), prescriptionEntity.isFeeIncluded(), userEntity.getAccountNumber(), userEntity.getAccountOwner(), FeatureToggleUtil.isLoggedUserPrescriptionBased());
            }
            prescriptionEntity.setOrderUrl(payment.getOrderUrl());
            prescriptionEntity.setPaymentToken(payment.getPaymentToken());
            sendPaymentRequestsViaEmailAndSms(prescriptionEntity);
        } catch (PaymentException | RestClientException e) {
            log.error("Communication with payment provider failed", e);
            prescriptionEntity.addError("P24: " + this.actualDateTime() + " " + e.getMessage());
        }
        this.prescriptionRepository.save(prescriptionEntity);
    }

    private boolean isP24PaymentProvider(UserEntity userEntity) {
        return userEntity.getPaymentProvider().equals(P_24_CODE);
    }

    public void sendPaymentRequestsViaEmailAndSms(PrescriptionEntity prescriptionEntity) {
        //TODO: WE look for a userEntity but in createPrescription flow we had this entity (createNewPrescription method)
        UserEntity userEntity = userRepository.findById(prescriptionEntity.getOwnerId()).get();
        String requestMessage = userEntity.getSmsMessageRequestPayment();
        String shortPaymentLink = FeatureToggleUtil.isUserServiceBased(userEntity) ? this.serviceBasedShortPaymentLink : this.prescriptionBasedShortPaymentLink;

        if(!Objects.equals(userEntity.getEmail(), prescriptionEntity.getEmail())) {
            sendEmailWithPaymentRequest(prescriptionEntity, requestMessage, shortPaymentLink);
        }
        sendSmsWithPaymentRequest(prescriptionEntity, requestMessage, shortPaymentLink);
    }

    private void sendSmsWithPaymentRequest(PrescriptionEntity prescriptionEntity, String requestMessage, String shortPaymentLink) {
        try {
            this.smsService.sendSms(requestMessage + shortPaymentLink + prescriptionEntity.getId(), prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        } catch (RuntimeException e) {
            log.error("Sending sms failed", e);
            prescriptionEntity.addError("SMS: " + this.actualDateTime() + " " + e.getMessage());
        }
    }

    private void sendEmailWithPaymentRequest(PrescriptionEntity prescriptionEntity, String requestMessage, String shortPaymentLink) {
        try {
            this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, requestMessage + shortPaymentLink + prescriptionEntity.getId());
        } catch (MessagingException | RuntimeException e) {
            log.error("Email could not be delivered", e);
            prescriptionEntity.addError("Email: " + this.actualDateTime() + " " + e.getMessage());
        }
    }


    public ZonedDateTime actualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Warsaw"));
        return zonedDateTime;
    }

    public PrescriptionEntity getPrescriptionWithChangedPrice(String id) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findById(id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }

        PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
        if (!prescriptionEntity.isFeeIncluded()) {
            prescriptionEntity.setPrice(prescriptionEntity.getPrice() + BMPaymentService.FEE);
        }

        return prescriptionEntity;
    }

    public Iterable<PrescriptionEntity> getAllPrescriptions() {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.prescriptionRepository.findByOwnerId(userEntity.getId());
    }

    public void updatePrescription(String id, PrescriptionEntity updateEntity) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findByOwnerIdAndId(userEntity.getId(), id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }


        PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
        if (updateEntity.getStatus().equals("COMPLETED")) {
            if (!StringUtils.isEmpty(updateEntity.getPrescriptionNumber())) {
                sendPrescriptionNumber(updateEntity, userEntity);
            } else {
                log.warn("Prescription {} changed to COMPLETED without prescription number filled", prescriptionEntity.getId());
            }
            if (isP24PaymentProvider(userEntity)) {
                p24PaymentService.sendPaymentToPartnerAndUs(prescriptionEntity);
            }
        } else if (prescriptionEntity.getStatus().equals("WAITING_FOR_CONFIRMATION") && updateEntity.getStatus().equals("CANCELED")) {
            if (isP24PaymentProvider(userEntity)) {
                p24PaymentService.cancelPayment(prescriptionEntity);
            }
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
        prescriptionEntity.setPrice(updateEntity.getPrice());
        prescriptionRepository.save(prescriptionEntity);
    }

    private void sendPrescriptionNumber(PrescriptionEntity prescriptionEntity, UserEntity userEntity) {
        String prescriptionReadyMessage = String.format(userEntity.getSmsMessageCompleted(), prescriptionEntity.getPrescriptionNumber());
        try {
            if(!Objects.equals(userEntity.getEmail(), prescriptionEntity.getEmail())) {
                this.emailService.sendSimpleMessage(prescriptionEntity.getEmail(), MAIL_SUBJECT, prescriptionReadyMessage);
            }
        } catch (MessagingException | RuntimeException e) {
            log.error("Email could not be delivered", e);
            prescriptionEntity.addError("Email: " + this.actualDateTime() + " " + e.getMessage());
        }
        try {
            this.smsService.sendSms(prescriptionReadyMessage, prescriptionEntity.getPhoneNumber(), MAIL_SUBJECT);
        } catch (RuntimeException e) {
            log.error("Sending sms failed", e);
            prescriptionEntity.addError("SMS: " + this.actualDateTime() + " " + e.getMessage());
        }
    }

}
