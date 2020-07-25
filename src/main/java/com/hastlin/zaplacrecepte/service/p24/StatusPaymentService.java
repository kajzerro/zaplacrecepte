package com.hastlin.zaplacrecepte.service.p24;

import com.hastlin.zaplacrecepte.model.dto.p24.P24StatusDto;
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

    public void changeStatusPayment(String paymentToken, P24StatusDto p24StatusDto) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = prescriptionRepository.findByPaymentToken(paymentToken);
        if (optionalPrescriptionEntity.isPresent()) {
            PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
            prescriptionEntity.setStatus("WAITING_FOR_CONFIRMATION");
            prescriptionRepository.save(prescriptionEntity);
        } else {
            log.warn("Unauthorized status change request to WAITING_FOR_CONFIRMATION for paymentToken={} and sign={}", paymentToken, p24StatusDto.getSign());
        }
    }

}
