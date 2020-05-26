package com.hastlin.zaplacrecepte.service.payu;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RedirectPaymentService {

    @Autowired
    PrescriptionRepository prescriptionRepository;

    public Optional<String> findRedirectUrl(String orderRedirectFromKey) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findByOrderRedirectFromKey(orderRedirectFromKey);
        if (optionalPrescriptionEntity.isPresent()) {
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            return Optional.of(prescriptionEntity.getOrderRedirectToUrl());
        } else {
            log.warn("Redirect from not existing key={}", orderRedirectFromKey);
            return Optional.empty();
        }
    }

}
