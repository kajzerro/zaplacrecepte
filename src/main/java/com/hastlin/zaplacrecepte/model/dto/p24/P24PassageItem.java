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
public class P24PassageItem {

  private String name;
  private String description;
  private int quantity;
  private int price;
  private Integer number;
  private int targetAmount;
  private int targetPosId;
}
