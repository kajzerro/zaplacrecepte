package com.hastlin.zaplacrecepte.service.payment.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BmPaymentServiceTest {

    @Test
    public void createPayment() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        System.out.println(bmPaymentService.createPayment(10));
    }

    @Test
    public void calculateHash() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        assertEquals("2ab52e6918c6ad3b69a8228a2ab815f11ad58533eeed963dd990df8d8c3709d1", bmPaymentService.calculateHash("2", "100", "1.50", "2test2"));
    }
}