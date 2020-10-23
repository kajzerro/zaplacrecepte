package com.hastlin.zaplacrecepte.model.dto.payment.bm;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BMStatusChangeRequestDto {
    private String transactions;
}
