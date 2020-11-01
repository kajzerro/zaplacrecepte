package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.service.payment.Payment;
import com.hastlin.zaplacrecepte.utils.BMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BMPaymentService {

    //TODO: To be migrated to yaml

    //    @Value("${bm.startTransactionUrl}")
    private String startTransactionUrl = "https://pay-accept.bm.pl/payment";

    //    @Value("${bm.serviceId}")
    private String serviceId = "903084";

    //    @Value("${bm.sharedKey}")
    private String sharedKey = "a76b59670d31b6d595c58d6e525bf689e6d8b7b4";

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
