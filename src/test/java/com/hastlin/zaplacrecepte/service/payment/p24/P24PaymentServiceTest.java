package com.hastlin.zaplacrecepte.service.payment.p24;

import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.service.payment.Payment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class P24PaymentServiceTest {

    @SpyBean
    P24PaymentService paymentService;

    @Before
    public void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserEntity userEntity = UserEntity.builder().username("someUserName").clientType("SERVICE_BASED").password(BCrypt.hashpw("someHardPassword", BCrypt.gensalt())).defaultPrice(30).build();
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldCreatePayment() {
        Payment payment = paymentService.createPayment("osika.jakub@gmail.com", 30);
        System.out.println(payment.getOrderUrl());
    }

    @Test
    public void shouldCreateCorrectSign() {
        assertEquals(paymentService.createSign("test", 118607, 123, "PLN", "e5ba4c3e5354171f"), "cde250840bf1e9ee16dbb8de10237e6861476bf7337ea3f7bf04846454ebb1608c179078f06120f8bcd892221988c4fa");
    }

    @Test
    public void shouldCalculateFeeCorrectly() {
        doNothing().when(paymentService).sendPayment(any(), anyString());
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().price(1234).build();
        paymentService.sendPaymentToPartnerAndUs(prescriptionEntity);
        verify(paymentService, times(1)).sendPayment(any(), Mockito.argThat(s ->
                (s.indexOf("119310") < s.indexOf("123100")) &&
                        (s.indexOf("123100") < s.indexOf("118681")) &&
                        (s.indexOf("118681") < s.indexOf("300"))));
    }

}