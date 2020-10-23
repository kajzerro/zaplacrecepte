package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDecodedDto;
import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDto;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.utils.BMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BMStatusPaymentService {

    //    @Value("${bm.serviceId}")
    private String serviceId = "903084";

    //    @Value("${bm.sharedKey}")
    String sharedKey = "a76b59670d31b6d595c58d6e525bf689e6d8b7b4";

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
        BMStatusChangeRequestDecodedDto decodedDto = parseRequest(decodeRequest(bmStatusChangeRequestDto));
        log.info("Got payment status change request \"{}\"", decodedDto);
        if (!isHashCorrect(decodedDto)) {
            log.warn("Unauthorized status change - wrong hash - request for request={}", decodedDto);
            return createNotConfirmedResponse(decodedDto);
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
        return !Objects.equals(serviceId, decodedDto.getServiceId()) ||
                !Objects.equals("PLN", decodedDto.getCurrency()) ||
                !optionalPrescriptionEntity.isPresent() ||
                !Objects.equals(BMUtils.formatPrice(optionalPrescriptionEntity.get().getPrice()), decodedDto.getAmount());
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
                sharedKey));
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
                hash(BMUtils.extractValueFromXmlTag(decodedRequest, "<hash>", "</hash>")).
                build();
    }

    private String decodeRequest(BMStatusChangeRequestDto bmStatusChangeRequestDto) {
        return new String((Base64.getDecoder().decode(bmStatusChangeRequestDto.getTransactions().getBytes())));
    }


}
