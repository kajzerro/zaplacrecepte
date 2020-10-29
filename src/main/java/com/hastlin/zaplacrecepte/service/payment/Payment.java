package com.hastlin.zaplacrecepte.service.payment;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payment {
    private String orderUrl;
    private String paymentToken;
}
