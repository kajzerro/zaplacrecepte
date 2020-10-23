package com.hastlin.zaplacrecepte.model.dto.payment.p24;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24OrderResult {

  private String token;
}
