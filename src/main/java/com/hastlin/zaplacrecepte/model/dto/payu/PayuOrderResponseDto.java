package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuOrderResponseDto {
    private PayuStatusDto status;
    private String redirectUri;
    private String orderId;
}
