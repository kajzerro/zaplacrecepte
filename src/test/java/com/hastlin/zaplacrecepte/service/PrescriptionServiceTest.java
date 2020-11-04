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

        UserEntity userEntity = UserEntity.builder().id("OWNER_ID").clientType("PRESCRIPTION_BASED").smsMessageRequestPayment("MESSAGE").build();
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().ownerId("OWNER_ID").build();
        when(userRepository.findById("OWNER_ID")).thenReturn(Optional.of(userEntity));

        prescriptionService.sendPaymentRequestsViaEmailAndSms(prescriptionEntity);

        verify(smsService, times(1)).sendSms(contains("MESSAGE"), any(), any());
        verify(emailService, times(1)).sendSimpleMessage(any(), any(), contains("MESSAGE"));
    }

}