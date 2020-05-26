package com.hastlin.zaplacrecepte.model.dto.payu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayuAuthorizeResponseDto {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String grant_type;
}
