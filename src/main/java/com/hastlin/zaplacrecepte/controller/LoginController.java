package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.LoginResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @GetMapping
    LoginResponseDto login() {
        return LoginResponseDto.builder().message("OK").build();
    }

}
