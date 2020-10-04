package com.hastlin.zaplacrecepte.repository;

import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PrescriptionRepositoryTest {


    @Autowired
    PrescriptionRepository repository;

    @After
    public void clearUp() {
        repository.deleteAll();
    }

    @Test
    public void should_save_record() {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("osa@osa.pl").phoneNumber("80768493").createDateTime(ZonedDateTime.now()).lastName("OSA").build();
        repository.save(prescriptionEntity);
        List<PrescriptionEntity> result = (List<PrescriptionEntity>) repository.findAll();
    }

    @Test
    public void should_get_all_records() {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("osa@osa.pl").createDateTime(ZonedDateTime.now().plusDays(10)).status("done").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity);
        PrescriptionEntity prescriptionEntity2 = PrescriptionEntity.builder().remarks("Przedłużenie recepty").status("paid").email("osa@osa.pl").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity2);
        List<PrescriptionEntity> result = (List<PrescriptionEntity>) repository.findAll();
    }

    @Test
    public void should_map_zoneddatetime_correctly() {
        try {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().paymentToken("paymentToken123").email("blaaa@osa.pl").createDateTime(ZonedDateTime.now().plusDays(10)).status("done").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity);
        Optional<PrescriptionEntity> paymentToken123 = repository.findByPaymentToken("paymentToken123");
        }
        catch (Exception e) {
            fail("Couldn't parse entity");
        }
    }

    @Test
    public void should_find_between_create_dates() {
        //given
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder()
                .paymentToken("paymentToken123")
                .email("zerk@osa.pl")
                .createDateTime(ZonedDateTime.of(2020, 9, 25, 23, 59, 20, 20, ZoneId.of("Europe/Warsaw")))
                .status("NEW").build();
        PrescriptionEntity prescriptionEntity2 = PrescriptionEntity.builder()
                .paymentToken("paymentToken123")
                .email("zerk@osa.pl")
                .createDateTime(ZonedDateTime.of(2020, 9, 26, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw")))
                .status("NEW").build();
        PrescriptionEntity prescriptionEntity3 = PrescriptionEntity.builder()
                .paymentToken("paymentToken123")
                .email("zerk@osa.pl")
                .createDateTime(ZonedDateTime.of(2020, 9, 25, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw")))
                .status("NEW").build();
        PrescriptionEntity prescriptionEntity4 = PrescriptionEntity.builder()
                .paymentToken("paymentToken123")
                .email("zerk@osa.pl")
                .createDateTime(ZonedDateTime.of(2020, 9, 25, 23, 59, 20, 20, ZoneId.of("Europe/Warsaw")))
                .status("notNEW").build();

        PrescriptionEntity prescriptionEntity5 = PrescriptionEntity.builder()
                .paymentToken("paymentToken123")
                .email("zerk@osa.pl")
                .createDateTime(ZonedDateTime.of(2020, 9, 25, 12, 59, 20, 20, ZoneId.of("Europe/Warsaw")))
                .status("NEW").build();
        //when
        repository.saveAll(Arrays.asList(prescriptionEntity, prescriptionEntity2, prescriptionEntity3, prescriptionEntity4, prescriptionEntity5));
        List<PrescriptionEntity> prescriptionEntities = repository.findByCreateDateTimeBetweenAndStatusEquals(ZonedDateTime.of(2020, 9, 25, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw")), ZonedDateTime.of(2020, 9, 25, 23, 59, 59, 999999999, ZoneId.of("Europe/Warsaw")), "NEW");

        //then
        assertEquals(3, prescriptionEntities.size());
        assertEquals(3, countAllEntitiesWithDayOfMonthEquals(prescriptionEntities, 25));
        assertEquals(3, countAllEntitiesWithStatusEquals(prescriptionEntities, "NEW"));
    }

    private long countAllEntitiesWithStatusEquals(List<PrescriptionEntity> prescriptionEntities, String expectedStatus) {
        return prescriptionEntities.stream().map(pe -> pe.getStatus()).filter(status -> status.equals(expectedStatus)).count();
    }

    private long countAllEntitiesWithDayOfMonthEquals(List<PrescriptionEntity> prescriptionEntities, int expectedDays) {
        return prescriptionEntities.stream().map(pe -> pe.getCreateDateTime().getDayOfMonth()).filter(days-> days == expectedDays).count();
    }

}