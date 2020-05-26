package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuProductDto {
    private String name;
    private String unitPrice;
    private String quantity;
}
