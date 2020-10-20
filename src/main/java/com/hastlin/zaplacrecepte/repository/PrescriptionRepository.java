package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, String> {

    Optional<PrescriptionEntity> findById(String id);

    Optional<PrescriptionEntity> findByPaymentToken(String paymentToken);

    List<PrescriptionEntity> findByOwnerId(String ownerId);

    List<PrescriptionEntity> findByCreateDateTimeBetweenAndStatusEquals(ZonedDateTime startDateTime, ZonedDateTime endDateTime, String status);
}