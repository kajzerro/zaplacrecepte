package com.hastlin.zaplacrecepte.service.payu;

import com.hastlin.zaplacrecepte.model.dto.payu.PayuAuthorizeResponseDto;
import com.hastlin.zaplacrecepte.model.dto.payu.PayuOrderRequestDto;
import com.hastlin.zaplacrecepte.model.dto.payu.PayuOrderResponseDto;
import com.hastlin.zaplacrecepte.model.dto.payu.PayuProductDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Service
public class CreatePaymentService {

    private static final String PAYMENT_HOST = "https://secure.snd.payu.com";
    private static final String CLIENT_ID = "386506";
    private static final String CLIENT_SECRET = "acd67c74de98157b5816bdaa3e8c83ee";
    private static final String MERCHANT_POS_ID = "386506";
    private static final String TOTAL_AMOUNT = "4500";

    public Payment createPayment(String clientIp) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayuAuthorizeResponseDto> response = authorize(restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccess_token());
        String paymentToken = UUID.randomUUID().toString();
        HttpEntity<PayuOrderRequestDto> payuOrderRequest = new HttpEntity<>(PayuOrderRequestDto.builder()
                .customerIp(clientIp)
                .notifyUrl("https://api.zaplacrecepte.pl/payment/notification/" + paymentToken)
                .continueUrl("https://www.zaplacrecepte.pl/afterPayment")
                .merchantPosId(MERCHANT_POS_ID)
                .description("Recepta")
                .currencyCode("PLN")
                .totalAmount(TOTAL_AMOUNT)
                .products(Collections.singletonList(PayuProductDto.builder().name("Kontynuacja").quantity("1").unitPrice(TOTAL_AMOUNT).build()))
                .build(), headers);

        ResponseEntity<PayuOrderResponseDto> payuOrderResponseDtoResponseEntity = restTemplate.exchange(PAYMENT_HOST + "/api/v2_1/orders", HttpMethod.POST, payuOrderRequest, PayuOrderResponseDto.class);
        PayuOrderResponseDto payuOrderResponseDto = payuOrderResponseDtoResponseEntity.getBody();
        return Payment.builder()
                .orderId(payuOrderResponseDto.getOrderId())
                .paymentToken(paymentToken)
                .orderRedirectToUrl(payuOrderResponseDto.getRedirectUri())
                .orderRedirectFromKey(createRedirectKey()).build();
    }

    private String createRedirectKey() {
        return "" + System.currentTimeMillis();
    }

    private ResponseEntity<PayuAuthorizeResponseDto> authorize(RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> accessTokenRequest = new HttpEntity<>(createAccessTokenRequest(CLIENT_ID, CLIENT_SECRET), headers);
        return restTemplate
                .exchange(PAYMENT_HOST + "/pl/standard/user/oauth/authorize", HttpMethod.POST, accessTokenRequest, PayuAuthorizeResponseDto.class);
    }

    private String createAccessTokenRequest(String clientId, String clientSecret) {
        return "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
    }

}
