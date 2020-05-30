package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface PrescriptionRepository extends CrudRepository<PrescriptionEntity, String> {

    Optional<PrescriptionEntity> findById(String id);

    Optional<PrescriptionEntity> findByPaymentTokenAndOrderId(String paymentToken, String orderId);

    Optional<PrescriptionEntity> findByPaymentToken(String paymentToken);

    Optional<PrescriptionEntity> findByOrderRedirectFromKey(String orderRedirectFromKey);
}