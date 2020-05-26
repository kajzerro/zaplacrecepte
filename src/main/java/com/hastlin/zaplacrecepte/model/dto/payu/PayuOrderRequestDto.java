package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuOrderRequestDto {
    private String orderId;
    private String customerIp;
    private String notifyUrl;
    private String continueUrl;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private List<PayuProductDto> products;
    private String status;
}
