package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionService {

    private static final String STATUS_UNPAID = "unpaid";

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public void createNewPrescription(PrescriptionEntity prescriptionEntity) {
        prescriptionEntity.setStatus(STATUS_UNPAID);
        this.prescriptionRepository.save(prescriptionEntity);
    }

    public Iterable<PrescriptionEntity> getAllPrescriptions() {
        return this.prescriptionRepository.findAll();
    }

    public void patchPrescription(String id, PrescriptionEntity patchedPrescriptionEntity) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findById(id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }

        PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
        prescriptionEntity.setStatus(patchedPrescriptionEntity.getStatus());
        prescriptionRepository.save(prescriptionEntity);
    }
}
