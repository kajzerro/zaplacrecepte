package com.hastlin.zaplacrecepte.controller.payment;

import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDto;
import com.hastlin.zaplacrecepte.service.payment.bm.BMStatusPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BMPaymentController {

    @Autowired
    BMStatusPaymentService bmStatusPaymentService;

    @PostMapping(path = "/payment/notification/bluemedia", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> notification(BMStatusChangeRequestDto bmStatusChangeRequestDto) {
        log.info("Received bm notification request {}", bmStatusChangeRequestDto);
        return ResponseEntity.ok(bmStatusPaymentService.changeStatusPayment(bmStatusChangeRequestDto));
    }

}
