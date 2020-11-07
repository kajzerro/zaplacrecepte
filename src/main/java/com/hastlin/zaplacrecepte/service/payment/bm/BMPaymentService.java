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
    public static final int FEE = 3;
    public static final String SERVICE_BASED_DESCRIPTION = "Opłata za usługę medyczną";
    public static final String PRESCRIPTION_BASED_DESCRIPTION = "Opłata za recepte";

    @Value("${payment.bm.startTransactionUrl}")
    private String startTransactionUrl;

    @Value("${payment.bm.serviceId}")
    private String serviceId;

    @Value("${payment.bm.sharedKey}")
    private String sharedKey;

    public Payment createPayment(int price, boolean feeIncluded, String accountNumber, String accountOwner, boolean isPrescriptionBased) {
        int calculatedPrice = price;
        if (feeIncluded) {
            calculatedPrice = price - FEE;
        }
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        String title = SERVICE_BASED_DESCRIPTION;
        if (isPrescriptionBased) {
            orderId = "P" + orderId.substring(1);
            title = PRESCRIPTION_BASED_DESCRIPTION;
        }
        String hash = BMUtils.calcHash(serviceId, orderId, BMUtils.formatPrice(calculatedPrice), accountNumber, title, accountOwner, sharedKey);

        return Payment.builder().orderUrl(startTransactionUrl +
                "?ServiceID=" + serviceId +
                "&OrderID=" + orderId +
                "&Amount=" + BMUtils.formatPrice(calculatedPrice) +
                "&CustomerNRB=" + accountNumber +
                "&Title=" + title +
                "&ReceiverName=" + accountOwner +
                "&Hash=" + hash).paymentToken(orderId).build();
    }

}
