package com.hastlin.zaplacrecepte.model.mapper;

import com.hastlin.zaplacrecepte.model.dto.PrescriptionDTO;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Created by mateuszkaszyk on 18/05/2020.
 */
@Service
public class PrescriptionMapper {

    public PrescriptionEntity toEntity(PrescriptionDTO prescriptionDTO) {
        return PrescriptionEntity.builder()
                .firstName(prescriptionDTO.getFirstName())
                .lastName(prescriptionDTO.getLastName())
                .pesel(prescriptionDTO.getPesel())
                .remarks(prescriptionDTO.getRemarks())
                .postalCode(prescriptionDTO.getPostalCode())
                .email(prescriptionDTO.getEmail())
                .phoneNumber(prescriptionDTO.getPhoneNumber())
                .status(prescriptionDTO.getStatus())
                .prescriptionNumber(prescriptionDTO.getPrescriptionNumber())
                .price(prescriptionDTO.getPrice())
                .build();
    }

    public PrescriptionDTO toDto(PrescriptionEntity prescriptionEntity) {
        return PrescriptionDTO.builder()
                .id(prescriptionEntity.getId())
                .firstName(prescriptionEntity.getFirstName())
                .lastName(prescriptionEntity.getLastName())
                .pesel(prescriptionEntity.getPesel())
                .remarks(prescriptionEntity.getRemarks())
                .postalCode(prescriptionEntity.getPostalCode())
                .email(prescriptionEntity.getEmail())
                .phoneNumber(prescriptionEntity.getPhoneNumber())
                .status(prescriptionEntity.getStatus())
                .createDateTime(prescriptionEntity.getCreateDateTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .prescriptionNumber(prescriptionEntity.getPrescriptionNumber())
                .orderUrl(prescriptionEntity.getOrderUrl())
                .price(prescriptionEntity.getPrice())
                .build();
    }
}
