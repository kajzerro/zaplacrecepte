package com.hastlin.zaplacrecepte.service.p24;

import com.hastlin.zaplacrecepte.model.dto.p24.P24OrderRequestDto;
import com.hastlin.zaplacrecepte.model.dto.p24.P24OrderResponseDto;
import com.hastlin.zaplacrecepte.service.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_384;

@Service
@Slf4j
public class PaymentService {

    @Value("${p24.host}")
    private String host;

    @Value("${p24.clientId}")
    private int clientId;

    @Value("${p24.crc}")
    private String crc;

    @Value("${p24.apiKey}")
    private String apiKey;

    @Value("${p24.notifyUrl}")
    private String notifyUrl;

    @Value("${p24.continueUrl}")
    private String continueUrl;

    private static final int TOTAL_AMOUNT = 4500;

    public Payment createPayment(String email) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(String.valueOf(clientId), apiKey);
        String paymentToken = UUID.randomUUID().toString();
        HttpEntity<P24OrderRequestDto> p24OrderRequest = new HttpEntity<>(P24OrderRequestDto.builder()
                .merchantId(clientId)
                .posId(clientId)
                .sessionId(paymentToken)
                .amount(TOTAL_AMOUNT)
                .currency("PLN")
                .description("Opłata za recepte")
                .email(email)
                .country("PL")
                .language("pl")
                .urlReturn(this.continueUrl)
                .urlStatus(this.notifyUrl + paymentToken)
                .timeLimit(0)
                .channel(16)
                .transferLabel("Opłata za recepte")
                .encoding("UTF-8")
                .sign(createSign(paymentToken, clientId, TOTAL_AMOUNT, "PLN", crc))
                .build(), headers);

        ResponseEntity<P24OrderResponseDto> payuOrderResponseDtoResponseEntity = restTemplate.exchange(this.host + "/api/v1/transaction/register", HttpMethod.POST, p24OrderRequest, P24OrderResponseDto.class);
        if (payuOrderResponseDtoResponseEntity.getStatusCode().isError()) {
            log.error("Payment provider returned code {} and message {}", payuOrderResponseDtoResponseEntity.getStatusCode(), payuOrderResponseDtoResponseEntity.getBody());
            String body = payuOrderResponseDtoResponseEntity.hasBody() ? payuOrderResponseDtoResponseEntity.getBody().toString() : "Body not provided";
            throw new PaymentException(body);
        }
        P24OrderResponseDto p24OrderResponseDto = payuOrderResponseDtoResponseEntity.getBody();
        log.info("Created payment with orderUrl {}", p24OrderResponseDto.getData().getToken());
        return Payment.builder()
                .orderUrl(host + "/trnRequest/" + p24OrderResponseDto.getData().getToken())
                .paymentToken(paymentToken).build();
    }

    String createSign(String sessionId, int merchantId, int amount, String currency, String crc) {
        String dataToEncode = "{\"sessionId\":\""+sessionId+"\",\"merchantId\":"+merchantId+",\"amount\":"+amount+",\"currency\":\""+currency+"\",\"crc\":\""+crc+"\"}";
        return new DigestUtils(SHA_384).digestAsHex(dataToEncode);
    }

    public void cancelPayment(String orderId) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<PayuAuthorizeResponseDto> response = authorize(restTemplate);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(response.getBody().getAccess_token());
//        HttpEntity<Void> cancelRequest = new HttpEntity<>(headers);
//        ResponseEntity<PayuResponseDto> responseDto = restTemplate.exchange(this.host + "/api/v2_1/orders/" + orderUrl, HttpMethod.DELETE, cancelRequest, PayuResponseDto.class);
//        log.info("Cancelled payment with orderUrl {} with response {}", orderUrl, responseDto.getBody().getStatus().getStatusCode());
    }

}
