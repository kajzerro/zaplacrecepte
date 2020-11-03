package com.hastlin.zaplacrecepte.service.payment.bm;

import org.junit.Test;

public class BmPaymentServiceTest {

    @Test
    public void createPaymentPrescription() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        System.out.println(bmPaymentService.createPayment(10, "99999889898989", "Kuba Osa", true));
    }

    @Test
    public void createPaymentService() {
        BMPaymentService bmPaymentService = new BMPaymentService();
        System.out.println(bmPaymentService.createPayment(10, "99999999999999", "Kuba Osa", false));
    }

}