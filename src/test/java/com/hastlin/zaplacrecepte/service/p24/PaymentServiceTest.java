package com.hastlin.zaplacrecepte.service.p24;

import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    public void shouldCreatePayment() {
        Payment payment = paymentService.createPayment("osika.jakub@gmail.com");
        System.out.println(payment.getOrderUrl());
    }

    @Test
    public void shouldCreateCorrectSign() {
        assertEquals(paymentService.createSign("test", 118607, 123, "PLN", "e5ba4c3e5354171f"), "cde250840bf1e9ee16dbb8de10237e6861476bf7337ea3f7bf04846454ebb1608c179078f06120f8bcd892221988c4fa");
    }
}