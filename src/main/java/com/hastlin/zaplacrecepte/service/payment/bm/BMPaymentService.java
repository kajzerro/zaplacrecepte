package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.service.payment.Payment;
import com.hastlin.zaplacrecepte.utils.BMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BMPaymentService {

    @Value("${payment.bm.startTransactionUrl}")
    private String startTransactionUrl;

    @Value("${payment.bm.serviceId}")
    private String serviceId;

    @Value("${payment.bm.sharedKey}")
    private String sharedKey;

    public Payment createPayment(int price, boolean isPrescriptionBased) {
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        if (isPrescriptionBased) {
            orderId = "P" + orderId.substring(1);
        }
        String hash = calculateHash(serviceId, orderId, BMUtils.formatPrice(price), sharedKey);
        return Payment.builder().orderUrl(startTransactionUrl + "?ServiceID=" + serviceId + "&OrderID=" + orderId + "&Amount=" + BMUtils.formatPrice(price) + "&" +
                "Hash=" + hash).paymentToken(orderId).build();
    }

    String calculateHash(String serviceId, String orderId, String amount, String sharedKey) {
        return BMUtils.calcHash(serviceId, orderId, amount, sharedKey);
    }


}
