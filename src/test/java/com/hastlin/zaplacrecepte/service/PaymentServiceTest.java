package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.service.payu.Payment;
import com.hastlin.zaplacrecepte.service.payu.PaymentService;
import org.junit.Test;

public class PaymentServiceTest {

    @Test
    public void shouldCreatePayment() {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.createPayment("127.0.0.1");
        System.out.println(payment.getOrderId());
        System.out.println(payment.getOrderRedirectToUrl());
        System.out.println(payment.getOrderRedirectFromKey());
    }
}