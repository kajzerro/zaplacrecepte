package com.hastlin.zaplacrecepte.service.payment.p24;

import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24CancelRequestDto;
import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24OrderRequestDto;
import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24OrderResponseDto;
import com.hastlin.zaplacrecepte.model.dto.payment.p24.P24RefundDto;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.service.exception.PaymentException;
import com.hastlin.zaplacrecepte.service.payment.Payment;
import com.hastlin.zaplacrecepte.utils.FeatureToggleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_384;

@Service
@Slf4j
public class P24PaymentService {

    public static final String SERVICE_BASED_DESCRIPTION = "Opłata za usługę medyczną";
    public static final String PRESCRIPTION_BASED_DESCRIPTION = "Opłata za recepte";
    @Value("${payment.p24.host}")
    private String host;

    @Value("${payment.p24.clientId}")
    private int clientId;

    @Value("${payment.p24.crc}")
    private String crc;

    @Value("${payment.p24.apiKey}")
    private String apiKey;

    @Value("${payment.p24.notifyUrl}")
    private String notifyUrl;

    @Value("${payment.p24.prescriptionBasedContinueUrl}")
    private String prescriptionBasedContinueUrl;

    @Value("${payment.p24.serviceBasedContinueUrl}")
    private String serviceBasedContinueUrl;

    @Value("${payment.p24.splitPaymentUrl:}")
    private String splitPaymentUrl;

    private static final int PARTNER_ID = 119310;
    private static final int OUR_ID = 118681;
    private static final int AMOUNT_FOR_US_PLN = 3;

    public Payment createPayment(String email, int price) {
        String description = FeatureToggleUtil.isServiceBased() ? SERVICE_BASED_DESCRIPTION : PRESCRIPTION_BASED_DESCRIPTION;
        String continueUrl = FeatureToggleUtil.isServiceBased() ? this.serviceBasedContinueUrl : this.prescriptionBasedContinueUrl;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(String.valueOf(clientId), apiKey);
        String paymentToken = UUID.randomUUID().toString();
        HttpEntity<P24OrderRequestDto> p24OrderRequest = new HttpEntity<>(P24OrderRequestDto.builder()
                .merchantId(clientId)
                .posId(clientId)
                .sessionId(paymentToken)
                .amount(price * 100)
                .currency("PLN")
                .description(description)
                .email(email)
                .country("PL")
                .language("pl")
                .urlReturn(continueUrl)
                .urlStatus(this.notifyUrl + paymentToken)
                .timeLimit(0)
                .channel(16)
                .transferLabel(description)
                .encoding("UTF-8")
                .sign(createSign(paymentToken, clientId, price * 100, "PLN", crc))
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
        String dataToEncode = "{\"sessionId\":\"" + sessionId + "\",\"merchantId\":" + merchantId + ",\"amount\":" + amount + ",\"currency\":\"" + currency + "\",\"crc\":\"" + crc + "\"}";
        return new DigestUtils(SHA_384).digestAsHex(dataToEncode);
    }

    public void cancelPayment(PrescriptionEntity prescriptionEntity) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(String.valueOf(clientId), apiKey);
        HttpEntity<P24CancelRequestDto> p24CancelRequestDto = new HttpEntity<>(P24CancelRequestDto.builder()
                .requestId(UUID.randomUUID().toString())
                .refundsUuid(UUID.randomUUID().toString())
                .refunds(Collections.singletonList(P24RefundDto.builder()
                        .orderId(Integer.parseInt(prescriptionEntity.getOrderId()))
                        .sessionId(prescriptionEntity.getPaymentToken())
                        .amount(prescriptionEntity.getPrice() * 100)
                        .description("Zwrot srodkow za recepte")
                        .build()))
                .build(), headers);
        log.info("About to cancel payment on {} with data={}", this.host + "/api/v1/transaction/refund", p24CancelRequestDto.getBody());
        ResponseEntity<String> response = restTemplate.exchange(this.host + "/api/v1/transaction/refund", HttpMethod.POST, p24CancelRequestDto, String.class);
        log.info("Cancelled payment with orderId {} with response {}", prescriptionEntity.getOrderId(), response.getBody());
    }

    public void sendPaymentToPartnerAndUs(PrescriptionEntity prescriptionEntity) {
        String splitPaymentToPartnerRequestBody = preparePaymentSoapRequest(prescriptionEntity, PARTNER_ID, (prescriptionEntity.getPrice() - AMOUNT_FOR_US_PLN) * 100, OUR_ID, AMOUNT_FOR_US_PLN * 100);
        this.sendPayment(prescriptionEntity, splitPaymentToPartnerRequestBody);
    }


    void sendPayment(PrescriptionEntity prescriptionEntity, String splitPaymentRequestBody) {
        if (!StringUtils.isEmpty(splitPaymentUrl)) {
            this.sendPaymentSoapRequest(prescriptionEntity, splitPaymentRequestBody);
        }
        else {
            log.info("Skip payment split as url is not provided");
        }
    }


    private String preparePaymentSoapRequest(PrescriptionEntity prescriptionEntity, int partnerAccountId, int partnerAmountToSend, int ourAccountId, int ourAmountToSend) {
        return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:php=\"https://secure.przelewy24.pl/external/118607.php\" xmlns:soap-enc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <php:DispatchTransaction soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "         <login xsi:type=\"xsd:string\">" + clientId + "</login>\n" +
                "         <pass xsi:type=\"xsd:string\">" + apiKey + "</pass>\n" +
                "         <batchId xsi:type=\"xsd:int\">" + prescriptionEntity.getOrderId() + "</batchId>\n" +
                "         <details xsi:type=\"php:ArrayOfDispatch\" soap-enc:arrayType=\"php:Dispatch[]\">\n" +
                "\t\t<Dispatch>\n" +
                "\t\t<orderId type=\"xsd:int\">" + prescriptionEntity.getOrderId() + "</orderId>\n" +
                "\t\t<sessionId type=\"xsd:string\">" + prescriptionEntity.getPaymentToken() + "</sessionId>\n" +
                "\t\t<sellerId type=\"xsd:int\">" + partnerAccountId + "</sellerId>\n" +
                "\t\t<amount type=\"xsd:int\">" + partnerAmountToSend + "</amount>\n" +
                "\t\t</Dispatch>\n" +
                "\t\t<Dispatch>\n" +
                "\t\t<orderId type=\"xsd:int\">" + prescriptionEntity.getOrderId() + "</orderId>\n" +
                "\t\t<sessionId type=\"xsd:string\">" + prescriptionEntity.getPaymentToken() + "</sessionId>\n" +
                "\t\t<sellerId type=\"xsd:int\">" + ourAccountId + "</sellerId>\n" +
                "\t\t<amount type=\"xsd:int\">" + ourAmountToSend + "</amount>\n" +
                "\t\t</Dispatch>\n" +
                "         </details>\n" +
                "      </php:DispatchTransaction>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    private void sendPaymentSoapRequest(PrescriptionEntity prescriptionEntity, String splitPaymentRequestBody) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/xml;charset=UTF-8");
        HttpEntity<String> splitPaymentRequest = new HttpEntity<>(splitPaymentRequestBody, headers);
        log.info("About to send split payment on {} with data={}", this.splitPaymentUrl, splitPaymentRequest);
        ResponseEntity<String> splitPaymentResponse = restTemplate.exchange(this.splitPaymentUrl, HttpMethod.POST, splitPaymentRequest, String.class);
        log.info("Payment split for orderId {} with response {}", prescriptionEntity.getOrderId(), splitPaymentResponse.getBody());
    }

}
