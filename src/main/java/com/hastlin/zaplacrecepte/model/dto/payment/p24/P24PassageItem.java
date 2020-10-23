package com.hastlin.zaplacrecepte.model.dto.payment.p24;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24PassageItem {

  private String name;
  private String description;
  private int quantity;
  private int price;
  private Integer number;
  private int targetAmount;
  private int targetPosId;
}
