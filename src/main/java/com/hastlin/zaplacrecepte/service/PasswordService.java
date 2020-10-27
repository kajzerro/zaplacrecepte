package com.hastlin.zaplacrecepte.service;


import com.hastlin.zaplacrecepte.model.dto.ChangePasswordDto;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;


    public void processChangingPassword(ChangePasswordDto changePasswordDto) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPasswordRepeated()) && BCrypt.checkpw(changePasswordDto.getOldPassword(), userEntity.getPassword())) {
            this.changePassword(userEntity, changePasswordDto.getNewPassword());
        } else {
            throw new AccessDeniedException("Wrong password or new passwords not the same");
        }
    }


    private void changePassword(UserEntity userEntity, String newPassword) {
        userEntity.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepository.save(userEntity);
    }
}
