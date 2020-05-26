package com.hastlin.zaplacrecepte.service.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payment {
    private String orderId;
    private String paymentToken;
    private String orderRedirectToUrl;
    private String orderRedirectFromKey;
}
