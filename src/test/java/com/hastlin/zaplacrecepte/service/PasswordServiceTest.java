package com.hastlin.zaplacrecepte.service;


import com.hastlin.zaplacrecepte.model.dto.ChangePasswordDto;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PasswordServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordService passwordService;

    @After
    public void cleanUp() {
        this.userRepository.deleteAll();
    }

    @Before
    public void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserEntity userEntity = UserEntity.builder().username("someUserName").clientType("SERVICE_BASED").password(BCrypt.hashpw("someHardPassword", BCrypt.gensalt())).defaultPrice(30).build();
        userRepository.save(userEntity);
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void should_change_password_when_good_old_password_and_both_new_password_same() {

        //when
        passwordService.processChangingPassword(ChangePasswordDto.builder().oldPassword("someHardPassword").newPassword("newPassword").newPasswordRepeated("newPassword").build());

        //then
        assertTrue(BCrypt.checkpw("newPassword",userRepository.findByUsername("someUserName").get().getPassword()));
    }

    @Test
    public void should_not_change_password_when_old_password_incorrect() {
        //when
        try {
            passwordService.processChangingPassword(ChangePasswordDto.builder().oldPassword("wrongPassword").newPassword("oneMoreNewPassword").newPasswordRepeated("oneMoreNewPassword").build());
            fail("Should throw exception before reaching this point");
        }
        //then
        catch (Exception e) {
            assertTrue(e instanceof AccessDeniedException);
            assertEquals(e.getMessage(), "Wrong password or new passwords not the same");
            assertTrue(BCrypt.checkpw("someHardPassword", userRepository.findByUsername("someUserName").get().getPassword()));
        }
    }

    @Test
    public void should_not_change_password_when_new_password_not_the_same() {
        //when
        try {
            passwordService.processChangingPassword(ChangePasswordDto.builder().oldPassword("someHardPassword").newPassword("newPassword1").newPasswordRepeated("newPassword2").build());
            fail("Should throw exception before reaching this point");
        }
        //then
        catch (Exception e) {
            assertTrue(e instanceof AccessDeniedException);
            assertEquals(e.getMessage(), "Wrong password or new passwords not the same");
            assertTrue(BCrypt.checkpw("someHardPassword", userRepository.findByUsername("someUserName").get().getPassword()));
        }
    }

}