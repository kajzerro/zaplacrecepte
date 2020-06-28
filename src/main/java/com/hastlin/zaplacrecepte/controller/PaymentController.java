package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.payu.PayuNotificationDto;
import com.hastlin.zaplacrecepte.service.payu.RedirectPaymentService;
import com.hastlin.zaplacrecepte.service.payu.StatusPaymentService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    StatusPaymentService statusPaymentService;
    @Autowired
    RedirectPaymentService redirectPaymentService;

    @PostMapping(path = "/payment/notification/{paymentToken}")
    public ResponseEntity patchPrescription(@PathVariable String paymentToken, @RequestBody PayuNotificationDto payuNotificationDto) {
        statusPaymentService.changeStatusPayment(paymentToken, payuNotificationDto.getOrder().getOrderId(), payuNotificationDto.getOrder().getStatus());
        return ResponseEntity.ok("OK");
    }

//    @GetMapping(path = "/r/{redirectKey}")
//    public String patchPrescription(@PathVariable String redirectKey, HttpServletResponse response) throws IOException {
//        Optional<String> redirect = redirectPaymentService.findRedirectUrl(redirectKey);
//        if (redirect.isPresent()) {
//            response.sendRedirect(redirect.get());
//            return "OK";
//        }
//        return "Wrong link";
//    }

    @GetMapping(path = "/mock/confirm/{prescriptionId}")
    public String patchPrescription(@PathVariable String prescriptionId, HttpServletResponse response) throws IOException {
        statusPaymentService.mockConfirmPayment(prescriptionId);
        return "Płatność zrealizowana";
    }
}
