package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import com.hastlin.zaplacrecepte.service.payment.bm.BMPaymentService;
import com.hastlin.zaplacrecepte.service.payment.p24.P24PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private P24PaymentService p24PaymentService;

    @Mock
    private BMPaymentService bmPaymentService;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @InjectMocks
    PrescriptionService prescriptionService;

    @Test
    public void shouldReturnActualDateTimeInISOFormat() {
        System.out.println(prescriptionService.actualDateTime());
        //2020-05-24T10:39:38.626+02:00
    }

    @Test
    public void shouldUseUserFromPrescriptionOwner() throws MessagingException {

        UserEntity userEntity = UserEntity.builder().id("OWNER_ID").email("owneremail@gmail.com").clientType("PRESCRIPTION_BASED").smsMessageRequestPayment("MESSAGE").build();
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("useremail@gmail.com").ownerId("OWNER_ID").build();
        when(userRepository.findById("OWNER_ID")).thenReturn(Optional.of(userEntity));

        prescriptionService.sendPaymentRequestsViaEmailAndSms(prescriptionEntity);

        verify(smsService, times(1)).sendSms(contains("MESSAGE"), any(), any());
        verify(emailService, times(1)).sendSimpleMessage(any(), any(), contains("MESSAGE"));
    }

    @Test
    public void should_not_send_email_with_payment_request_when_email_address_same_as_doctors_email() throws MessagingException {

        UserEntity userEntity = UserEntity.builder().id("OWNER_ID").email("someemail@gmail.com").clientType("PRESCRIPTION_BASED").smsMessageRequestPayment("MESSAGE").build();
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("someemail@gmail.com").ownerId("OWNER_ID").build();
        when(userRepository.findById("OWNER_ID")).thenReturn(Optional.of(userEntity));

        prescriptionService.sendPaymentRequestsViaEmailAndSms(prescriptionEntity);

        verify(smsService, times(1)).sendSms(contains("MESSAGE"), any(), any());
        verify(emailService, times(0)).sendSimpleMessage(any(), any(), contains("MESSAGE"));
    }

    @Test
    public void should_email_and_sms_on_update() throws MessagingException {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        UserEntity userEntity = UserEntity.builder().id("OWNER_ID").paymentProvider("BM").email("differentemail@gmail.com").clientType("PRESCRIPTION_BASED").smsMessageCompleted("MESSAGE").build();
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().id("someId").email("someemail@gmail.com").ownerId("OWNER_ID").status("WAITING_FOR_CONFIRMATION").build();
        PrescriptionEntity updateEntity = PrescriptionEntity.builder().id("someId").prescriptionNumber("1234").email("someemail@gmail.com").ownerId("OWNER_ID").status("COMPLETED").build();
        when(prescriptionRepository.findByOwnerIdAndId("OWNER_ID", "someId")).thenReturn(Optional.of(prescriptionEntity));

        //when
        prescriptionService.updatePrescription("someId", updateEntity);

        //then
        verify(smsService, times(1)).sendSms(contains("MESSAGE"), any(), any());
        verify(emailService, times(1)).sendSimpleMessage(any(), any(), contains("MESSAGE"));
        SecurityContextHolder.clearContext();
    }


    @Test
    public void should_not_send_email_on_update_when_email_address_same_as_doctors_email() throws MessagingException {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        UserEntity userEntity = UserEntity.builder().id("OWNER_ID").paymentProvider("BM").email("someemail@gmail.com").clientType("PRESCRIPTION_BASED").smsMessageCompleted("MESSAGE").build();
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().id("someId").email("someemail@gmail.com").ownerId("OWNER_ID").status("WAITING_FOR_CONFIRMATION").build();
        PrescriptionEntity updateEntity = PrescriptionEntity.builder().id("someId").prescriptionNumber("1234").email("someemail@gmail.com").ownerId("OWNER_ID").status("COMPLETED").build();
        when(prescriptionRepository.findByOwnerIdAndId("OWNER_ID", "someId")).thenReturn(Optional.of(prescriptionEntity));

        //when
        prescriptionService.updatePrescription("someId", updateEntity);

        //then
        verify(smsService, times(1)).sendSms(contains("MESSAGE"), any(), any());
        verify(emailService, times(0)).sendSimpleMessage(any(), any(), contains("MESSAGE"));
        SecurityContextHolder.clearContext();
    }



}