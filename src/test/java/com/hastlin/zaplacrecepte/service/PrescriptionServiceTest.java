package com.hastlin.zaplacrecepte.service;

import org.junit.Test;

public class PrescriptionServiceTest {

    @Test
    public void shouldReturnActualDateTimeInISOFormat() {
        PrescriptionService prescriptionService = new PrescriptionService();
        System.out.println(prescriptionService.actualDateTime());
        //2020-05-24T10:39:38.626+02:00
    }
}