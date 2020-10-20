package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserRepositoryTest {


    @Autowired
    UserRepository repository;

    @After
    public void clearUp() {
        repository.deleteAll();
    }

    @Test
    public void should_save_record() {
        UserEntity userEntity = UserEntity.builder().username("drkowalski").password("password123").firstName("Jan").lastName("Kowalski").build();
        repository.save(userEntity);
        List<UserEntity> result = repository.findAll();
        assertEquals(1, result.size());
    }


    @Test
    public void should_findByUsername() {
        UserEntity userEntity = UserEntity.builder().username("drkowalski").password("password123").firstName("Jan").lastName("Kowalski").build();
        repository.save(userEntity);

        UserEntity userEntity2 = UserEntity.builder().username("drnowak").password("admin123").firstName("Jan").lastName("Nowak").build();
        repository.save(userEntity2);

        Optional<UserEntity> user = repository.findByUsername("drkowalski");
        assertEquals("password123", user.get().getPassword());
        assertEquals("Kowalski", user.get().getLastName());
    }


}