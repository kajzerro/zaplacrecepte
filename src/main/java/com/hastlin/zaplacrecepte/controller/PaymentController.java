package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.p24.P24StatusDto;
import com.hastlin.zaplacrecepte.service.p24.StatusPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    StatusPaymentService statusPaymentService;

    @PostMapping(path = "/payment/notification/p24/{paymentToken}")
    public ResponseEntity patchPrescription(@PathVariable String paymentToken, @RequestBody P24StatusDto p24StatusDto) {
        statusPaymentService.changeStatusPayment(paymentToken, p24StatusDto);
        return ResponseEntity.ok("OK");
    }

}
