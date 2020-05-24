package com.hastlin.zaplacrecepte.service;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PrescriptionService {

    private static final String STATUS_UNPAID = "unpaid";

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public void createNewPrescription(PrescriptionEntity prescriptionEntity) {
        prescriptionEntity.setStatus(STATUS_UNPAID);
        prescriptionEntity.setCreateDateTime(actualDateTime());
        this.prescriptionRepository.save(prescriptionEntity);
    }

    String actualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Europe/Warsaw"));
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public Iterable<PrescriptionEntity> getAllPrescriptions() {
        return this.prescriptionRepository.findAll();
    }

    public void updatePrescription(String id, PrescriptionEntity updateEntity) {
        Optional<PrescriptionEntity> optionalPrescriptionEntity = this.prescriptionRepository.findById(id);

        if (!optionalPrescriptionEntity.isPresent()) {
            throw new RuntimeException("Prescription not found");
        }

        PrescriptionEntity prescriptionEntity = optionalPrescriptionEntity.get();
        prescriptionEntity.setFirstName(updateEntity.getFirstName());
        prescriptionEntity.setLastName(updateEntity.getLastName());
        prescriptionEntity.setPesel(updateEntity.getPesel());
        prescriptionEntity.setPostalCode(updateEntity.getPostalCode());
        prescriptionEntity.setRemarks(updateEntity.getRemarks());
        prescriptionEntity.setPhoneNumber(updateEntity.getPhoneNumber());
        prescriptionEntity.setEmail(updateEntity.getEmail());
        prescriptionEntity.setStatus(updateEntity.getStatus());
        prescriptionRepository.save(prescriptionEntity);
    }
}
