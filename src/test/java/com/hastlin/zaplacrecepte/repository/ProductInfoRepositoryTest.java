package com.hastlin.zaplacrecepte.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hastlin.zaplacrecepte.ZaplacrecepteApplication;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZaplacrecepteApplication.class)
@WebAppConfiguration
public class ProductInfoRepositoryTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    PrescriptionRepository repository;

    @Test
    public void should_save_record() {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("osa@osa.pl").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity);
        List<PrescriptionEntity> result = (List<PrescriptionEntity>) repository.findAll();
    }

    @Test
    public void should_get_all_records() {
        PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder().email("osa@osa.pl").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity);
        PrescriptionEntity prescriptionEntity2 = PrescriptionEntity.builder().remarks("Przedłużenie recepty").email("osa@osa.pl").phoneNumber("80768493").lastName("OSA").build();
        repository.save(prescriptionEntity2);
        List<PrescriptionEntity> result = (List<PrescriptionEntity>) repository.findAll();
        System.out.println(result);
    }
}