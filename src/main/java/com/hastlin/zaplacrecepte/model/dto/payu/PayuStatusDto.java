package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuStatusDto {
    private String statusCode;
    private String statusDesc;
}
