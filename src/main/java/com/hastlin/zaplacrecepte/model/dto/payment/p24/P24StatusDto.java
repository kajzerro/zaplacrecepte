package com.hastlin.zaplacrecepte.model.dto.payment.p24;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class P24StatusDto {

  private int merchantId;
  private int posId;
  private String sessionId;
  private int amount;
  private int originAmount;
  private String currency;
  private int orderId;
  private int methodId;
  private String statement;
  private String sign;
}
