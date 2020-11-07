package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.service.payment.Payment;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BmPaymentServiceTest {

    @Test
    public void createPaymentPrescription() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        Payment payment = bmPaymentService.createPayment(10, true, "99999889898989", "Kuba Osa", true);
        System.out.println(payment);
        assertTrue(payment.getOrderUrl().contains("7.00"));
    }

    @Test
    public void createPaymentService() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        Payment payment = bmPaymentService.createPayment(10, false, "99999999999999", "Kuba Osa", false);
        System.out.println(payment);
        assertTrue(payment.getOrderUrl().contains("10.00"));
    }

}