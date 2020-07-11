package com.hastlin.zaplacrecepte.model.dto.p24;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
