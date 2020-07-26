package com.hastlin.zaplacrecepte.model.dto.p24;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class P24CancelRequestDto {

    private String requestId;
    private List<P24RefundDto> refunds;
    private String refundsUuid;
}
