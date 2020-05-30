package com.hastlin.zaplacrecepte.service.payu;

import com.hastlin.zaplacrecepte.model.dto.payu.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

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
                .continueUrl("https://api.zaplacrecepte.pl/fake/" + paymentToken)
                .merchantPosId(MERCHANT_POS_ID)
                .description("Recepta")
                .currencyCode("PLN")
                .totalAmount(TOTAL_AMOUNT)
                .products(Collections.singletonList(PayuProductDto.builder().name("Kontynuacja").quantity("1").unitPrice(TOTAL_AMOUNT).build()))
                .build(), headers);

        ResponseEntity<PayuOrderResponseDto> payuOrderResponseDtoResponseEntity = restTemplate.exchange(PAYMENT_HOST + "/api/v2_1/orders", HttpMethod.POST, payuOrderRequest, PayuOrderResponseDto.class);
        PayuOrderResponseDto payuOrderResponseDto = payuOrderResponseDtoResponseEntity.getBody();
        log.info("Created payment with orderId {}", payuOrderResponseDto.getOrderId());
        return Payment.builder()
                .orderId(payuOrderResponseDto.getOrderId())
                .paymentToken(paymentToken)
                .orderRedirectToUrl(payuOrderResponseDto.getRedirectUri())
                .orderRedirectFromKey(createRedirectKey()).build();
    }

    public void cancelPayment(String orderId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayuAuthorizeResponseDto> response = authorize(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccess_token());
        HttpEntity<Void> cancelRequest = new HttpEntity<>(headers);
        ResponseEntity<PayuResponseDto> responseDto = restTemplate.exchange(PAYMENT_HOST + "/api/v2_1/orders/" + orderId, HttpMethod.DELETE, cancelRequest, PayuResponseDto.class);
        log.info("Cancelled payment with orderId {} with response {}", orderId, responseDto.getBody().getStatus().getStatusCode());
    }

    public void acceptPayment(String orderId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayuAuthorizeResponseDto> response = authorize(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccess_token());
        HttpEntity<PayuAcceptOrderDto> acceptRequest = new HttpEntity<>(PayuAcceptOrderDto.builder().orderId(orderId).orderStatus("COMPLETED").build(), headers);
        ResponseEntity<PayuResponseDto> responseDto = restTemplate.exchange(PAYMENT_HOST + "/api/v2_1/orders/" + orderId + "/status", HttpMethod.PUT, acceptRequest, PayuResponseDto.class);
        log.info("Accepted payment with orderId {} with response {}", orderId, responseDto.getBody().getStatus().getStatusCode());
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
