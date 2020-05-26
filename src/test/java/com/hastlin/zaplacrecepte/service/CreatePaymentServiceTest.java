package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.service.payu.CreatePaymentService;
import com.hastlin.zaplacrecepte.service.payu.Payment;
import org.junit.Test;

public class CreatePaymentServiceTest {

    @Test
    public void shouldCreatePayment() {
        CreatePaymentService createPaymentService = new CreatePaymentService();
        Payment payment = createPaymentService.createPayment("127.0.0.1");
        System.out.println(payment.getOrderId());
        System.out.println(payment.getOrderRedirectToUrl());
        System.out.println(payment.getOrderRedirectFromKey());
    }
}