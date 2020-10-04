package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@EnableScan
public interface PrescriptionRepository extends CrudRepository<PrescriptionEntity, String> {

    Optional<PrescriptionEntity> findById(String id);

    Optional<PrescriptionEntity> findByPaymentToken(String paymentToken);

    List<PrescriptionEntity> findByCreateDateTimeBetweenAndStatusEquals(ZonedDateTime startDateTime, ZonedDateTime endDateTime, String status);
}