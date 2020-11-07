package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.LoginResponseDto;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @GetMapping
    public LoginResponseDto login() {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return LoginResponseDto.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .clientType(userEntity.getClientType())
                .defaultPrice(userEntity.getDefaultPrice())
                .feeIncluded(userEntity.isFeeIncluded())
                .build();
    }

}
