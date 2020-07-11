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
public class P24OrderRequestDto {

  private int merchantId;
  private String crc;
  private String sessionId;
  private int amount;
  private String currency;
  private String description;
  private String email;
  private String country;
  private String client;
  private String address;
  private String zip;
  private String city;
  private String phone;
  private String language;
  private int method;
  private String urlStatus;
  private int timeLimit;
  private int channel;
  private int shipping;
  private String transferLabel;
  private P24PassageCart passageCart;
  private String methodRefId;
}
