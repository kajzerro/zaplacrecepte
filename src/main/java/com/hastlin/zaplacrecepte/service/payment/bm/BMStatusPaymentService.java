package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDecodedDto;
import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDto;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.utils.BMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class BMStatusPaymentService {

    @Value("${payment.bm.serviceId}")
    String serviceId;

    @Value("${payment.bm.sharedKey}")
    String sharedKey;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    private final String RESPONSE_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<confirmationList>\n" +
            "<serviceID>SERVICE_ID</serviceID>\n" +
            "<transactionsConfirmations>\n" +
            "<transactionConfirmed>\n" +
            "<orderID>ORDER_ID</orderID>\n" +
            "<confirmation>CONFIRMATION</confirmation>\n" +
            "</transactionConfirmed>\n" +
            "</transactionsConfirmations>\n" +
            "<hash>HASH</hash>\n" +
            "</confirmationList>";

    public String changeStatusPayment(BMStatusChangeRequestDto bmStatusChangeRequestDto) {
        String unparsedRequest = decodeRequest(bmStatusChangeRequestDto);
        BMStatusChangeRequestDecodedDto decodedDto = parseRequest(unparsedRequest);
        log.info("Got payment status change request \"{}\"", decodedDto);
        if (!isHashCorrect(decodedDto)) {
            log.info("Unauthorized status change - wrong DTOhash - request for request={}", decodedDto);
        }
        try {
            log.info("Calculate unparsed hash");
            if (!isHashInUnparsedCorrect(unparsedRequest)) {
                log.warn("Unauthorized status change - wrong unparsedHash - request for request={}", unparsedRequest);
            }
        } catch (RuntimeException e) {
            log.error("Calculating hash from unparsed request error", e);
        }

        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findByPaymentToken(decodedDto.getOrderId());
        if (contentDoesntMatch(decodedDto, optionalPrescriptionEntity)
        ) {
            log.warn("Unauthorized status change - values dont match - request for request={}", decodedDto);
            return createNotConfirmedResponse(decodedDto);
        }
        if ("SUCCESS".equals(decodedDto.getPaymentStatus())) {
            log.info("Handling status SUCCESS");
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            prescriptionEntity.setStatus("WAITING_FOR_CONFIRMATION");
            prescriptionEntity.setOrderId(decodedDto.getRemoteId());
            prescriptionRepository.save(prescriptionEntity);
        } else {
            log.info("Skipping handle status {}", decodedDto.getPaymentStatus());
        }
        return createConfirmedResponse(decodedDto);
    }

    private boolean contentDoesntMatch(BMStatusChangeRequestDecodedDto decodedDto, Optional<PrescriptionEntity> optionalPrescriptionEntity) {
        String storedPrice = BMUtils.formatPrice(optionalPrescriptionEntity.get().getPrice());
        return !Objects.equals(serviceId, decodedDto.getServiceId()) ||
                !Objects.equals("PLN", decodedDto.getCurrency()) ||
                !optionalPrescriptionEntity.isPresent() ||
                !(Objects.equals(storedPrice, decodedDto.getAmount()) ||
                        Objects.equals(storedPrice, decodedDto.getStartAmount()));
    }

    String createConfirmationResponse(BMStatusChangeRequestDecodedDto decodedDto, String confirmation) {
        return RESPONSE_TEMPLATE.replaceAll("SERVICE_ID", decodedDto.getServiceId())
                .replaceAll("ORDER_ID", decodedDto.getOrderId())
                .replaceAll("CONFIRMATION", confirmation)
                .replaceAll("HASH", BMUtils.calcHash(decodedDto.getServiceId(), decodedDto.getOrderId(), confirmation, sharedKey));

    }

    String createNotConfirmedResponse(BMStatusChangeRequestDecodedDto decodedDto) {
        return createConfirmationResponse(decodedDto, "NOTCONFIRMED");
    }

    String createConfirmedResponse(BMStatusChangeRequestDecodedDto decodedDto) {
        return createConfirmationResponse(decodedDto, "CONFIRMED");
    }

    boolean isHashCorrect(BMStatusChangeRequestDecodedDto dto) {
        return Objects.equals(dto.getHash(), BMUtils.calcHash(dto.getServiceId(),
                dto.getOrderId(),
                dto.getRemoteId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getGatewayId(),
                dto.getPaymentDate(),
                dto.getPaymentStatus(),
                dto.getPaymentStatusDetails(),
                dto.getFName(),
                dto.getLName(),
                dto.getStreetName(),
                dto.getStreetHouseNo(),
                dto.getStreetStaircaseNo(),
                dto.getStreetPremiseNo(),
                dto.getPostalCode(),
                dto.getCity(),
                dto.getNrb(),
                dto.getSenderData(),
                dto.getStartAmount(),
                sharedKey));
    }

    boolean isHashInUnparsedCorrect(String unparsedRequest) {
        String hash = BMUtils.extractValueFromXmlTag(unparsedRequest, "<hash>", "</hash>");
        Pattern pattern = Pattern.compile("<\\w+>([^<]*)<\\/\\w+>");
        List<String> valueNodes = new ArrayList<>();
        Matcher m = pattern.matcher(unparsedRequest);
        while (m.find()) {
            if (!Objects.equals(hash, m.group(1)))
                valueNodes.add(m.group(1));
        }
        valueNodes.add(sharedKey);
        return Objects.equals(hash, BMUtils.calcHash(valueNodes.toArray(new String[0])));
    }

    BMStatusChangeRequestDecodedDto parseRequest(String decodedRequest) {
        return BMStatusChangeRequestDecodedDto.builder().
                serviceId(BMUtils.extractValueFromXmlTag(decodedRequest, "<serviceID>", "</serviceID>")).
                orderId(BMUtils.extractValueFromXmlTag(decodedRequest, "<orderID>", "</orderID>")).
                remoteId(BMUtils.extractValueFromXmlTag(decodedRequest, "<remoteID>", "</remoteID>")).
                amount(BMUtils.extractValueFromXmlTag(decodedRequest, "<amount>", "</amount>")).
                currency(BMUtils.extractValueFromXmlTag(decodedRequest, "<currency>", "</currency>")).
                gatewayId(BMUtils.extractValueFromXmlTag(decodedRequest, "<gatewayID>", "</gatewayID>")).
                paymentDate(BMUtils.extractValueFromXmlTag(decodedRequest, "<paymentDate>", "</paymentDate>")).
                paymentStatus(BMUtils.extractValueFromXmlTag(decodedRequest, "<paymentStatus>", "</paymentStatus>")).
                paymentStatusDetails(BMUtils.extractValueFromXmlTag(decodedRequest, "<paymentStatusDetails>", "</paymentStatusDetails>")).
                fName(BMUtils.extractValueFromXmlTag(decodedRequest, "<fName>", "</fName>")).
                lName(BMUtils.extractValueFromXmlTag(decodedRequest, "<lName>", "</lName>")).
                streetName(BMUtils.extractValueFromXmlTag(decodedRequest, "<streetName>", "</streetName>")).
                streetHouseNo(BMUtils.extractValueFromXmlTag(decodedRequest, "<streetHouseNo>", "</streetHouseNo>")).
                streetStaircaseNo(BMUtils.extractValueFromXmlTag(decodedRequest, "<streetStaircaseNo>", "</streetStaircaseNo>")).
                streetPremiseNo(BMUtils.extractValueFromXmlTag(decodedRequest, "<streetPremiseNo>", "</streetPremiseNo>")).
                postalCode(BMUtils.extractValueFromXmlTag(decodedRequest, "<postalCode>", "</postalCode>")).
                city(BMUtils.extractValueFromXmlTag(decodedRequest, "<city>", "</city>")).
                nrb(BMUtils.extractValueFromXmlTag(decodedRequest, "<nrb>", "</nrb>")).
                senderData(BMUtils.extractValueFromXmlTag(decodedRequest, "<senderData>", "</senderData>")).
                startAmount(BMUtils.extractValueFromXmlTag(decodedRequest, "<startAmount>", "</startAmount>")).
                hash(BMUtils.extractValueFromXmlTag(decodedRequest, "<hash>", "</hash>")).
                build();
    }

    private String decodeRequest(BMStatusChangeRequestDto bmStatusChangeRequestDto) {
        return new String((Base64.getDecoder().decode(bmStatusChangeRequestDto.getTransactions().getBytes())));
    }


}
