package com.hastlin.zaplacrecepte.model.dto.payment.p24;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24OrderResponseDto {

  private P24OrderResult data;
  private String responseCode;
}
