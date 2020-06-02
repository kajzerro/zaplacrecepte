package com.hastlin.zaplacrecepte.service.payu;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StatusPaymentService {

    @Autowired
    PrescriptionRepository prescriptionRepository;

    public void changeStatusPayment(String paymentToken, String orderId, String status) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findByPaymentTokenAndOrderId(paymentToken, orderId);
        if (optionalPrescriptionEntity.isPresent()) {
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            prescriptionEntity.setStatus(status);
            prescriptionRepository.save(prescriptionEntity);
        } else {
            log.warn("Unauthorized status change request to {} for orderId={}, paymentToken={}", status, orderId, paymentToken);
        }
    }

}
