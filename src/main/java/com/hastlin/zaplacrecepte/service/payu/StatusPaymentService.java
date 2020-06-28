package com.hastlin.zaplacrecepte.service.payu;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void mockConfirmPayment(String prescriptionId) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findById(prescriptionId);
        if (optionalPrescriptionEntity.isPresent()) {
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            prescriptionEntity.setStatus("WAITING_FOR_CONFIRMATION");
            prescriptionRepository.save(prescriptionEntity);
            log.info("MOCK confirmation OK for id={}", prescriptionId);
        } else {
            log.warn("MOCK confirmation FAILED for id={}", prescriptionId);
        }
    }

}
