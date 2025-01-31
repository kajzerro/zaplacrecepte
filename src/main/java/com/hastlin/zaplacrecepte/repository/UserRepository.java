package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findById(String id);

    Optional<UserEntity> findByUsername(String username);

}