package com.hastlin.zaplacrecepte.model.dto.payment.p24;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class P24PassageCart {

  private List<P24PassageItem> items = new ArrayList<>();
}
