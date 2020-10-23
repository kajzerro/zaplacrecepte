package com.hastlin.zaplacrecepte.model.dto.payment.p24;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24StatusConfirmationRequestDto {

  private int merchantId;
  private int posId;
  private String sessionId;
  private int amount;
  private String currency;
  private int orderId;
  private String sign;
}
