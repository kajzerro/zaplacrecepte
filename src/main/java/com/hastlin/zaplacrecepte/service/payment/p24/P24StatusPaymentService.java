package com.hastlin.zaplacrecepte.service.payment.p24;

import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24StatusConfirmationRequestDto;
import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24StatusConfirmationResponseDto;
import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24StatusDto;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_384;

@Service
@Slf4j
public class P24StatusPaymentService {

    @Value("${p24.crc}")
    private String crc;

    @Value("${p24.host}")
    private String host;

    @Value("${p24.clientId}")
    private int clientId;

    @Value("${p24.apiKey}")
    private String apiKey;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    public void changeStatusPayment(String paymentToken, P24StatusDto p24StatusDto) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findByPaymentToken(paymentToken);
        //TODO: isAuthorized(p24StatusDto)
        if (optionalPrescriptionEntity.isPresent()) {
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            prescriptionEntity.setStatus("WAITING_FOR_CONFIRMATION");
            prescriptionEntity.setOrderId(String.valueOf(p24StatusDto.getOrderId()));
            prescriptionRepository.save(prescriptionEntity);
            confirmStatus(p24StatusDto);
        } else {
            log.warn("Unauthorized status change request to WAITING_FOR_CONFIRMATION for paymentToken={} and sign={}", paymentToken, p24StatusDto.getSign());
        }
    }

    private void confirmStatus(P24StatusDto p24StatusDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(String.valueOf(clientId), apiKey);
        HttpEntity<P24StatusConfirmationRequestDto> p24StatusConfirmationRequestDto = new HttpEntity<>(P24StatusConfirmationRequestDto.builder()
                .merchantId(p24StatusDto.getMerchantId())
                .posId(p24StatusDto.getPosId())
                .sessionId(p24StatusDto.getSessionId())
                .amount(p24StatusDto.getAmount())
                .currency(p24StatusDto.getCurrency())
                .orderId(p24StatusDto.getOrderId())
                .sign(createConfirmationSign(p24StatusDto.getSessionId(), p24StatusDto.getAmount(), p24StatusDto.getCurrency(), p24StatusDto.getOrderId(), crc))
                .build(), headers);
        log.info("About to send confirmation to {} with data={}", this.host + "/api/v1/transaction/verify", p24StatusConfirmationRequestDto.getBody());
        ResponseEntity<P24StatusConfirmationResponseDto> payuOrderResponseDtoResponseEntity = restTemplate.exchange(this.host + "/api/v1/transaction/verify", HttpMethod.PUT, p24StatusConfirmationRequestDto, P24StatusConfirmationResponseDto.class);
        log.info("Status change confirmation for sessionId={} orderId={} response status={}", p24StatusDto.getSessionId(), p24StatusDto.getOrderId(), payuOrderResponseDtoResponseEntity.getBody().getData().getStatus());
    }

    boolean isAuthorized(P24StatusDto p24StatusDto) {
        return p24StatusDto.getSign().equals(createVerificationSign(p24StatusDto.getMerchantId(), p24StatusDto.getPosId(), p24StatusDto.getSessionId(), p24StatusDto.getAmount(), p24StatusDto.getOriginAmount(), p24StatusDto.getCurrency(), p24StatusDto.getOrderId(), p24StatusDto.getMethodId(), p24StatusDto.getStatement(), crc));
    }

    String createVerificationSign(int merchantId, int posId, String sessionId, int amount, int originAmount, String currency, int orderId, int methodId, String statement, String crc) {
        String dataToEncode = "{\"merchantId\": " + merchantId + ", \"posId\": " + posId + ", \"sessionId\": \"" + sessionId + "\", \"amount\": " + amount + ", \"originAmount\": " + originAmount + ", \"currency\": \"" + currency + "\", \"orderId\": " + orderId + ", \"methodId\": " + methodId + ", \"statement\": " + statement + ", \"crc\": \"" + crc + "\"}";
        return new DigestUtils(SHA_384).digestAsHex(dataToEncode);
    }

    String createConfirmationSign(String sessionId, int amount, String currency, int orderId, String crc) {
        String dataToEncode = "{\"sessionId\":\"" + sessionId + "\",\"orderId\":" + orderId + ",\"amount\":" + amount + ",\"currency\":\"" + currency + "\",\"crc\":\"" + crc + "\"}";
        return new DigestUtils(SHA_384).digestAsHex(dataToEncode);
    }

}
