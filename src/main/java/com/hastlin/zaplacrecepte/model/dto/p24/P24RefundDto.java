package com.hastlin.zaplacrecepte.model.dto.p24;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class P24RefundDto {

    private int orderId;
    private String sessionId;
    private int amount;
    private String description;
}
