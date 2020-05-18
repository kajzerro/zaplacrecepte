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
    private String message;
}
