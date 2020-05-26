package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuAcceptOrderDto {
    private String orderId;
    private String orderStatus;
}
