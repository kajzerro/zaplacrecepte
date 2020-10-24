package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.ChangePasswordDto;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.service.PasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChangePasswordController {

    @Autowired
    private PasswordService passwordService;

    @PatchMapping(path= "/api/password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        log.info("Received changePassword request for user {}",((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        this.passwordService.processChangingPassword(changePasswordDto);
        return ResponseEntity.ok("OK");
    }

}
