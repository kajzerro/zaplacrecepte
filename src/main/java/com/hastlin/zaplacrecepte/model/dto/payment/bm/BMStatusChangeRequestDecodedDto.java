package com.hastlin.zaplacrecepte.model.dto.payment.bm;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BMStatusChangeRequestDecodedDto {
    private String serviceId;
    private String orderId;
    private String remoteId;
    private String amount;
    private String currency;
    private String gatewayId;
    private String paymentDate;
    private String paymentStatus;
    private String paymentStatusDetails;
    //customerData
    private String fName;
    private String lName;
    private String streetName;
    private String streetHouseNo;
    private String streetStaircaseNo;
    private String streetPremiseNo;
    private String postalCode;
    private String city;
    private String nrb;
    private String senderData;

    private String hash;
}
