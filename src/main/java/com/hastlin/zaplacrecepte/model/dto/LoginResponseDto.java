package com.hastlin.zaplacrecepte.model.dto;

import lombok.*;

/**
 * Created by mateuszkaszyk on 17/05/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String clientType;
    private Integer defaultPrice;
}
