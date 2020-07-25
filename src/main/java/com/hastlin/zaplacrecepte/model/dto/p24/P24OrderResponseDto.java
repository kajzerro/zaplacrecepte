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
public class P24OrderResponseDto {

  private P24OrderResult data;
  private String responseCode;
}
