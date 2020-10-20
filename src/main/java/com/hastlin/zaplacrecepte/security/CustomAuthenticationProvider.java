package com.hastlin.zaplacrecepte.security;

import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();


        prescriptionRepository.findByPaymentToken(username);
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (!userEntity.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        if (BCrypt.checkpw(password, userEntity.get().getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userEntity.get(), password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Wrong username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}