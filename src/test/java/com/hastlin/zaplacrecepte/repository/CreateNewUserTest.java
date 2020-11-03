package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.ZonedDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
@ActiveProfiles("demo")
public class CreateNewUserTest {


    @Autowired
    UserRepository repository;

    @Ignore
    @Test
    public void should_add_new_user1() {
        UserEntity userEntity = UserEntity.builder()
                .username("drkrzystyniak")
                .password(BCrypt.hashpw("jankes", BCrypt.gensalt()))
                .firstName("Marek")
                .lastName("Krzystyniak")
                .email("krzyampagabinet@outlook.com")
                .createDateTime(ZonedDateTime.now())
                .phoneNumber("")
                .smsMessageRequestPayment("Dr Marek Krzystyniak prosi o oplacenie recepty: ")
                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
                .build();
        repository.save(userEntity);
    }

    @Ignore
    @Test
    public void should_add_new_user2() {
        UserEntity userEntity = UserEntity.builder()
                .username("drpaliga")
                .password(BCrypt.hashpw("aligator964", BCrypt.gensalt()))
                .firstName("Jakub")
                .lastName("Paliga")
                .email("")
                .createDateTime(ZonedDateTime.now())
                .phoneNumber("")
                .smsMessageRequestPayment("Dr Jakub Paliga prosi o oplacenie porady medycznej: ")
                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
                .build();
        repository.save(userEntity);
    }

    @Ignore
    @Test
    public void should_add_new_user3() {
        UserEntity userEntity = UserEntity.builder()
                .username("arturm@mp.pl")
                .password(BCrypt.hashpw("arturm120", BCrypt.gensalt()))
                .clientType("SERVICE_BASED")
                .defaultPrice(0)
                .firstName("Artur")
                .lastName("Marcinkiewicz")
                .phoneNumber("604641120")
                .email("arturm@mp.pl")
                .smsMessageRequestPayment("Dr Artur Marcinkiewicz prosi o oplacenie uslugi medycznej: ")
                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
                .paymentProvider("BM")
                .createDateTime(ZonedDateTime.now())
                .accountNumber("87102023680000260200963074")
                .accountOwner("Artur Marcinkiewicz")
                .build();
        repository.save(userEntity);
    }

}