package com.hastlin.zaplacrecepte.model.dto.p24;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24OrderRequestDto {

  private int merchantId;
  private int posId;
  private String sessionId;
  private int amount;
  private String currency;
  private String description;
  private String email;
  private String country;
  private String language;
  private String urlReturn;
  private String urlStatus;
  private int timeLimit;
  private int channel;
  private String transferLabel;
  private String sign;
  private String encoding;
}
