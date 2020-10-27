package com.hastlin.zaplacrecepte.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
    private String newPasswordRepeated;
}
