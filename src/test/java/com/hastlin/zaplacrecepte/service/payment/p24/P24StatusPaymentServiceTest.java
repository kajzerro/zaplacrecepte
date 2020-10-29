package com.hastlin.zaplacrecepte.service.payment.p24;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_384;

public class P24StatusPaymentServiceTest {

    @Test
    public void createConfirmationSignTest() {
        int merchantId = 118607;
        int posId = 118607;
        String sessionId = "910fe582-2b82-43c0-879b-9dcf9ff5a190";
        int amount = 4500;
        String currency = "PLN";
        int orderId = 305772010;
        String crc = "373004a537f5c0f9";
        String dataToEncode = "{\"sessionId\":\"" + sessionId + "\",\"orderId\":" + orderId + ",\"amount\":" + amount + ",\"currency\":\"" + currency + "\",\"crc\":\"" + crc + "\"}";
        System.out.println(new DigestUtils(SHA_384).digestAsHex(dataToEncode));
    }
}