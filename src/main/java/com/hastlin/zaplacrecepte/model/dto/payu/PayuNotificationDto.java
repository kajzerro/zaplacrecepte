package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuNotificationDto {
    private PayuOrderRequestDto order;
    private String localReceiptDateTime;
}
