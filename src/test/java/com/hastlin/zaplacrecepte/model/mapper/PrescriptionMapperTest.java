package com.hastlin.zaplacrecepte.model.mapper;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class PrescriptionMapperTest {

    @Test
    public void should_format_create_time_in_correct_way() {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().createDateTime(ZonedDateTime.of(2020, 9, 26, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))).build();

        PrescriptionMapper prescriptionMapper = new PrescriptionMapper();

        assertEquals("2020-09-26T00:00:00+02:00", prescriptionMapper.toDto(prescriptionEntity).getCreateDateTime());
    }
}