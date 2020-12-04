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

    @Ignore
    @Test
    public void should_add_new_to_prod() {
//        UserEntity userEntity = UserEntity.builder()
//                .username("iwona@neuromag.med.pl")
//                .email("iwona@neuromag.med.pl")
//                .phoneNumber("668485964")
//                .password(BCrypt.hashpw("iwona964", BCrypt.gensalt()))
//                .firstName("Iwona")
//                .lastName("Gałaszek")
//                .smsMessageRequestPayment("Iwona Gałaszek prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("BM")
//                .defaultPrice(0)
//                .accountNumber("35102025280000000204632149")
//                .accountOwner("Neuromag Sp zoo")
//                .build();
//        repository.save(userEntity);
//
//        UserEntity userEntity = UserEntity.builder()
//                .username("maciek@neuromag.med.pl")
//                .email("maciek@neuromag.med.pl")
//                .phoneNumber("602730895")
//                .password(BCrypt.hashpw("maciek895", BCrypt.gensalt()))
//                .firstName("Maciej")
//                .lastName("Gałaszek")
//                .smsMessageRequestPayment("Dr Maciej Gałaszek prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("BM")
//                .defaultPrice(0)
//                .accountNumber("35102025280000000204632149")
//                .accountOwner("Neuromag Sp zoo")
//                .build();
//        repository.save(userEntity);
//
//        UserEntity userEntity = UserEntity.builder()
//                .username("paligakk@interia.pl")
//                .email("paligakk@interia.pl")
//                .phoneNumber("607322307")
//                .password(BCrypt.hashpw("paligakk307", BCrypt.gensalt()))
//                .firstName("Jakub")
//                .lastName("Paliga")
//                .smsMessageRequestPayment("Dr Jakub Paliga prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("BM")
//                .defaultPrice(150)
//                .accountNumber("50124041551111000046290128")
//                .accountOwner("Jakub Paliga")
//                .build();
//        repository.save(userEntity);
//        UserEntity userEntity = UserEntity.builder()
//                .username("e.dyrda@onet.eu")
//                .email("e.dyrda@onet.eu")
//                .phoneNumber("603195964")
//                .password(BCrypt.hashpw("e.dyrda964", BCrypt.gensalt()))
//                .firstName("Ewa")
//                .lastName("Dyrda")
//                .smsMessageRequestPayment("Dr Ewa Dyrda prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("BM")
//                .defaultPrice(30)
//                .feeIncluded(true)
//                .accountNumber("98109016520000000106733109")
//                .accountOwner("Ewa Dyrda")
//                .build();
//        repository.save(userEntity);
//        UserEntity userEntity = UserEntity.builder()
//            .username("januszwrozek@interia.pl")
//            .email("januszwrozek@interia.pl")
//            .phoneNumber("601471024")
//            .password(BCrypt.hashpw("januszwrozek024", BCrypt.gensalt()))
//            .firstName("Janusz")
//            .lastName("Wróżek")
//            .smsMessageRequestPayment("Dr Janusz Wróżek prosi o oplacenie recepty: ")
//            .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//            .createDateTime(ZonedDateTime.now())
//            .clientType("PRESCRIPTION_BASED")
//            .paymentProvider("BM")
//            .defaultPrice(100)
//            .feeIncluded(true)
//            .accountNumber("66102029060000110200154831")
//            .accountOwner("Janusz Wrozek")
//            .build();
//        repository.save(userEntity);

    }

    @Ignore
    @Test
    public void should_add_new_user_to_demo() {
//        UserEntity userEntity = UserEntity.builder()
//                .username("adrnow@gmail.com")
//                .email("zaplacrecepte+adriannowak@gmail.com")
//                .phoneNumber("111111111")
//                .password(BCrypt.hashpw("ph66g6bbN3Am", BCrypt.gensalt()))
//                .firstName("Adrian")
//                .lastName("Nowak")
//                .smsMessageRequestPayment("Dr Adrian Nowak prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("BM")
//                .feeIncluded(true)
//                .defaultPrice(10)
//                .accountNumber("43114020040000310258391535")
//                .accountOwner("Adrian Nowak")
//                .build();
//        repository.save(userEntity);
//        UserEntity userEntity = UserEntity.builder()
//                .username("drdyrda")
//                .email("zaplacrecepte+drdyrda@gmail.com")
//                .phoneNumber("111111111")
//                .password(BCrypt.hashpw("test", BCrypt.gensalt()))
//                .firstName("Ewa")
//                .lastName("Dyrda")
//                .smsMessageRequestPayment("Dr Ewa Dyrda prosi o oplacenie uslugi medycznej: ")
//                .smsMessageCompleted("Recepta zostala wystawiona. Kod: %s. Zrealizujesz recepte w aptece podajac kod i numer PESEL.")
//                .createDateTime(ZonedDateTime.now())
//                .clientType("SERVICE_BASED")
//                .paymentProvider("P24")
//                .feeIncluded(true)
//                .defaultPrice(10)
//                .accountNumber("43114020040000310258391535")
//                .accountOwner("Ewa Dyrda")
//                .build();
//        repository.save(userEntity);

    }

}