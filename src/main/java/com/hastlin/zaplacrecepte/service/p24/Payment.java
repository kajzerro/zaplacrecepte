package com.hastlin.zaplacrecepte.service.p24;

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
