package com.hastlin.zaplacrecepte.controller.payment;

import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24StatusDto;
import com.hastlin.zaplacrecepte.service.payment.p24.P24StatusPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class P24PaymentController {

    @Autowired
    private P24StatusPaymentService p24StatusPaymentService;

    @PostMapping(path = "/payment/notification/p24/{paymentToken}")
    public ResponseEntity notification(@PathVariable String paymentToken, @RequestBody P24StatusDto p24StatusDto) {
        log.info("Received p24 notification request {}", p24StatusDto);
        p24StatusPaymentService.changeStatusPayment(paymentToken, p24StatusDto);
        return ResponseEntity.ok("OK");
    }

}
