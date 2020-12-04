package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDecodedDto;
import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDto;
import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Base64;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class BMStatusPaymentServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    BMStatusPaymentService bmStatusPaymentService;

    @Test
    public void should_parse_request_from_documentation() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();

        String messageToParse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transactionList>\n" +
                "<serviceID>1</serviceID>\n" +
                "<transactions>\n" +
                "<transaction>\n" +
                "<orderID>11</orderID>\n" +
                "<remoteID>91</remoteID>\n" +
                "<amount>11.11</amount>\n" +
                "<currency>PLN</currency>\n" +
                "<gatewayID>1</gatewayID>\n" +
                "<paymentDate>20010101111111</paymentDate>\n" +
                "<paymentStatus>SUCCESS</paymentStatus>\n" +
                "<paymentStatusDetails>AUTHORIZED</paymentStatusDetails>\n" +
                "</transaction>\n" +
                "</transactions>\n" +
                "<hash>a103bfe581a938e9ad78238cfc674ffafdd6ec70cb6825e7ed5c41787671efe4</hash>\n" +
                "</transactionList>";

        assertEquals(BMStatusChangeRequestDecodedDto.builder().
                        serviceId("1").
                        orderId("11").
                        remoteId("91").
                        amount("11.11").
                        currency("PLN").
                        gatewayId("1").
                        paymentDate("20010101111111").
                        paymentStatus("SUCCESS").
                        paymentStatusDetails("AUTHORIZED").
                        hash("a103bfe581a938e9ad78238cfc674ffafdd6ec70cb6825e7ed5c41787671efe4").
                        build(),
                bmStatusPaymentService.parseRequest(messageToParse)
        );
    }

    @Test
    public void should_parse_real_request_from_demo() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();

        String messageToParse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<transactionList>\n" +
                "    <serviceID>903084</serviceID>\n" +
                "    <transactions>\n" +
                "        <transaction>\n" +
                "            <orderID>c46ae8502b0d4e1bab42aa0e0582aea1</orderID>\n" +
                "            <remoteID>AHUDQHGBWW</remoteID>\n" +
                "            <amount>30.00</amount>\n" +
                "            <currency>PLN</currency>\n" +
                "            <gatewayID>106</gatewayID>\n" +
                "            <paymentDate>20201020144958</paymentDate>\n" +
                "            <paymentStatus>SUCCESS</paymentStatus>\n" +
                "            <paymentStatusDetails>AUTHORIZED</paymentStatusDetails>\n" +
                "            <customerData>\n" +
                "                <fName>Jan</fName>\n" +
                "                <lName>Kowalski</lName>\n" +
                "                <streetName>Jasna</streetName>\n" +
                "                <streetHouseNo>6</streetHouseNo>\n" +
                "                <streetStaircaseNo>A</streetStaircaseNo>\n" +
                "                <streetPremiseNo>3</streetPremiseNo>\n" +
                "                <postalCode>10-234</postalCode>\n" +
                "                <city>Warszawa</city>\n" +
                "                <nrb>96109010301793218160815294</nrb>\n" +
                "            </customerData>\n" +
                "        </transaction>\n" +
                "    </transactions>\n" +
                "    <hash>a9e5b28b028380db73d06dd78185b3ab9b9864875127dad3baa6c892c633754b</hash>\n" +
                "</transactionList>";

        assertEquals(BMStatusChangeRequestDecodedDto.builder().
                        serviceId("903084").
                        orderId("c46ae8502b0d4e1bab42aa0e0582aea1").
                        remoteId("AHUDQHGBWW").
                        amount("30.00").
                        currency("PLN").
                        gatewayId("106").
                        paymentDate("20201020144958").
                        paymentStatus("SUCCESS").
                        paymentStatusDetails("AUTHORIZED").
                        fName("Jan").
                        lName("Kowalski").
                        streetName("Jasna").
                        streetHouseNo("6").
                        streetStaircaseNo("A").
                        streetPremiseNo("3").
                        postalCode("10-234").
                        city("Warszawa").
                        nrb("96109010301793218160815294").
                        hash("a9e5b28b028380db73d06dd78185b3ab9b9864875127dad3baa6c892c633754b").
                        build(),
                bmStatusPaymentService.parseRequest(messageToParse)
        );
    }

    @Test
    public void should_pass_correct_hash_from_documentation() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "1test1";
        assertTrue(bmStatusPaymentService.isHashCorrect(BMStatusChangeRequestDecodedDto.builder().
                serviceId("1").
                orderId("11").
                remoteId("91").
                amount("11.11").
                currency("PLN").
                gatewayId("1").
                paymentDate("20010101111111").
                paymentStatus("SUCCESS").
                paymentStatusDetails("AUTHORIZED").
                hash("a103bfe581a938e9ad78238cfc674ffafdd6ec70cb6825e7ed5c41787671efe4").
                build()));
    }

    @Test
    public void should_pass_correct_hash_from_demo() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "a76b59670d31b6d595c58d6e525bf689e6d8b7b4";
        assertTrue(bmStatusPaymentService.isHashCorrect(BMStatusChangeRequestDecodedDto.builder().
                serviceId("903084").
                orderId("c46ae8502b0d4e1bab42aa0e0582aea1").
                remoteId("AHUDQHGBWW").
                amount("30.00").
                currency("PLN").
                gatewayId("106").
                paymentDate("20201020144958").
                paymentStatus("SUCCESS").
                paymentStatusDetails("AUTHORIZED").
                fName("Jan").
                lName("Kowalski").
                streetName("Jasna").
                streetHouseNo("6").
                streetStaircaseNo("A").
                streetPremiseNo("3").
                postalCode("10-234").
                city("Warszawa").
                nrb("96109010301793218160815294").
                hash("a9e5b28b028380db73d06dd78185b3ab9b9864875127dad3baa6c892c633754b").
                build()));
    }

    @Test
    public void should_pass_correct_hash_from_success_prod() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "51473d84c2c6e0294a6f05457c6e1f13dedf6f68";
        assertTrue(bmStatusPaymentService.isHashCorrect(BMStatusChangeRequestDecodedDto.builder().
                serviceId("102652").
                orderId("8cff1c0b2b614010b2b56aacc94ba37d").
                remoteId("A6W8JUU8TH").
                amount("4.00").
                currency("PLN").
                gatewayId("3").
                paymentDate("20201102214432").
                paymentStatus("SUCCESS").
                paymentStatusDetails("AUTHORIZED").
                startAmount("1.00").
                hash("d8c9bf3b120234ab7a90d9e8280719be5430e5025d727066ff4d9b22b216851f").
                build()));
    }

    @Test
    public void should_pass_correct_hash_from_fail_prod() {
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "51473d84c2c6e0294a6f05457c6e1f13dedf6f68";
        assertTrue(bmStatusPaymentService.isHashCorrect(BMStatusChangeRequestDecodedDto.builder().
                serviceId("102652").
                orderId("8cff1c0b2b614010b2b56aacc94ba37d").
                remoteId("A5FMUAUJRG").
                amount("4.00").
                currency("PLN").
                paymentDate("20201102214053").
                paymentStatus("FAILURE").
                paymentStatusDetails("REJECTED_BY_USER").
                startAmount("1.00").
                hash("f64f18c697fe687da6f1c6c150ba54fc6392301d8291ab2386da6fb3c48785c6").
                build()));
    }

    @Test
    public void should_pass_hash_in_unparsed_request1() {
        String unparsedRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<transactionList>\n" +
                "    <serviceID>102652</serviceID>\n" +
                "    <transactions>\n" +
                "        <transaction>\n" +
                "            <orderID>8cff1c0b2b614010b2b56aacc94ba37d</orderID>\n" +
                "            <remoteID>A6W8JUU8TH</remoteID>\n" +
                "            <amount>4.00</amount>\n" +
                "            <currency>PLN</currency>\n" +
                "            <gatewayID>3</gatewayID>\n" +
                "            <paymentDate>20201102214432</paymentDate>\n" +
                "            <paymentStatus>SUCCESS</paymentStatus>\n" +
                "            <paymentStatusDetails>AUTHORIZED</paymentStatusDetails>\n" +
                "            <startAmount>1.00</startAmount>\n" +
                "        </transaction>\n" +
                "    </transactions>\n" +
                "    <hash>d8c9bf3b120234ab7a90d9e8280719be5430e5025d727066ff4d9b22b216851f</hash>\n" +
                "</transactionList>";
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "51473d84c2c6e0294a6f05457c6e1f13dedf6f68";
        assertTrue(bmStatusPaymentService.isHashInUnparsedCorrect(unparsedRequest));
    }

    @Test
    public void should_pass_hash_in_unparsed_request2() {
        String unparsedRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<transactionList>\n" +
                "    <serviceID>102652</serviceID>\n" +
                "    <transactions>\n" +
                "        <transaction>\n" +
                "            <orderID>8cff1c0b2b614010b2b56aacc94ba37d</orderID>\n" +
                "            <remoteID>A5FMUAUJRG</remoteID>\n" +
                "            <amount>4.00</amount>\n" +
                "            <currency>PLN</currency>\n" +
                "            <paymentDate>20201102214053</paymentDate>\n" +
                "            <paymentStatus>FAILURE</paymentStatus>\n" +
                "            <paymentStatusDetails>REJECTED_BY_USER</paymentStatusDetails>\n" +
                "            <startAmount>1.00</startAmount>\n" +
                "        </transaction>\n" +
                "    </transactions>\n" +
                "    <hash>f64f18c697fe687da6f1c6c150ba54fc6392301d8291ab2386da6fb3c48785c6</hash>\n" +
                "</transactionList>";
        BMStatusPaymentService bmStatusPaymentService = new BMStatusPaymentService();
        bmStatusPaymentService.sharedKey = "51473d84c2c6e0294a6f05457c6e1f13dedf6f68";
        assertTrue(bmStatusPaymentService.isHashInUnparsedCorrect(unparsedRequest));
    }

    @Test
    public void should_check_both_amounts() {

        String baseMessage = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8dHJhbnNhY3Rpb25MaXN0PgogICAgPHNlcnZpY2VJRD4xMDI2NTI8L3NlcnZpY2VJRD4KICAgIDx0cmFuc2FjdGlvbnM+CiAgICAgICAgPHRyYW5zYWN0aW9uPgogICAgICAgICAgICA8b3JkZXJJRD45MDY4OWFmN2EyMzE0ZjkyYThhMTNjMTM1MjkwN2Q4ZDwvb3JkZXJJRD4KICAgICAgICAgICAgPHJlbW90ZUlEPkExRVY4Q0xCNE08L3JlbW90ZUlEPgogICAgICAgICAgICA8YW1vdW50PjMwMC4wMDwvYW1vdW50PgogICAgICAgICAgICA8Y3VycmVuY3k+UExOPC9jdXJyZW5jeT4KICAgICAgICAgICAgPGdhdGV3YXlJRD45NTwvZ2F0ZXdheUlEPgogICAgICAgICAgICA8cGF5bWVudERhdGU+MjAyMDEyMDExNjIyNDM8L3BheW1lbnREYXRlPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1cz5TVUNDRVNTPC9wYXltZW50U3RhdHVzPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1c0RldGFpbHM+QVVUSE9SSVpFRDwvcGF5bWVudFN0YXR1c0RldGFpbHM+CiAgICAgICAgICAgIDxzdGFydEFtb3VudD4yOTcuMDA8L3N0YXJ0QW1vdW50PgogICAgICAgIDwvdHJhbnNhY3Rpb24+CiAgICA8L3RyYW5zYWN0aW9ucz4KICAgIDxoYXNoPmFkYTAyZGMxOTQ1YjEzNjg4MTdjNDM2ODVhMWE3M2I1YzVmYzg2YmQ4NzFiODhjNjMzYTg0OTRmMmI2YmRiYjY8L2hhc2g+CjwvdHJhbnNhY3Rpb25MaXN0Pgo=";
        String unparsedRequest = new String((Base64.getDecoder().decode(baseMessage.getBytes())));
        System.out.println(unparsedRequest);

        when(prescriptionRepository.findByPaymentToken(any())).thenReturn(Optional.of(PrescriptionEntity.builder()
                .id("8326665b-1846-4e35-8186-e9d543383b1e")
                .paymentToken("90689af7a2314f92a8a13c1352907d8d")
                .price(300)
                .build()));
        bmStatusPaymentService.serviceId = "102652";

        String confirmationMessage = bmStatusPaymentService.changeStatusPayment(BMStatusChangeRequestDto.builder().transactions(baseMessage).build());
        System.out.println(confirmationMessage);
        assertFalse(confirmationMessage.contains("NOTCONFIRMED"));
        assertTrue(confirmationMessage.contains("CONFIRMED"));
    }

    @Test
    public void should_check_both_amounts_and_fail_if_not_match() {

        String baseMessage = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8dHJhbnNhY3Rpb25MaXN0PgogICAgPHNlcnZpY2VJRD4xMDI2NTI8L3NlcnZpY2VJRD4KICAgIDx0cmFuc2FjdGlvbnM+CiAgICAgICAgPHRyYW5zYWN0aW9uPgogICAgICAgICAgICA8b3JkZXJJRD45MDY4OWFmN2EyMzE0ZjkyYThhMTNjMTM1MjkwN2Q4ZDwvb3JkZXJJRD4KICAgICAgICAgICAgPHJlbW90ZUlEPkExRVY4Q0xCNE08L3JlbW90ZUlEPgogICAgICAgICAgICA8YW1vdW50PjMwMC4wMDwvYW1vdW50PgogICAgICAgICAgICA8Y3VycmVuY3k+UExOPC9jdXJyZW5jeT4KICAgICAgICAgICAgPGdhdGV3YXlJRD45NTwvZ2F0ZXdheUlEPgogICAgICAgICAgICA8cGF5bWVudERhdGU+MjAyMDEyMDExNjIyNDM8L3BheW1lbnREYXRlPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1cz5TVUNDRVNTPC9wYXltZW50U3RhdHVzPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1c0RldGFpbHM+QVVUSE9SSVpFRDwvcGF5bWVudFN0YXR1c0RldGFpbHM+CiAgICAgICAgICAgIDxzdGFydEFtb3VudD4yOTcuMDA8L3N0YXJ0QW1vdW50PgogICAgICAgIDwvdHJhbnNhY3Rpb24+CiAgICA8L3RyYW5zYWN0aW9ucz4KICAgIDxoYXNoPmFkYTAyZGMxOTQ1YjEzNjg4MTdjNDM2ODVhMWE3M2I1YzVmYzg2YmQ4NzFiODhjNjMzYTg0OTRmMmI2YmRiYjY8L2hhc2g+CjwvdHJhbnNhY3Rpb25MaXN0Pgo=";
        String unparsedRequest = new String((Base64.getDecoder().decode(baseMessage.getBytes())));
        System.out.println(unparsedRequest);

        when(prescriptionRepository.findByPaymentToken(any())).thenReturn(Optional.of(PrescriptionEntity.builder()
                .id("8326665b-1846-4e35-8186-e9d543383b1e")
                .paymentToken("90689af7a2314f92a8a13c1352907d8d")
                .price(299)
                .build()));
        bmStatusPaymentService.serviceId = "102652";

        String confirmationMessage = bmStatusPaymentService.changeStatusPayment(BMStatusChangeRequestDto.builder().transactions(baseMessage).build());
        System.out.println(confirmationMessage);
        assertTrue(confirmationMessage.contains("NOTCONFIRMED"));
    }

    @Test
    public void deodeBase64message() {
        String baseMessage = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8dHJhbnNhY3Rpb25MaXN0PgogICAgPHNlcnZpY2VJRD4xMDI2NTI8L3NlcnZpY2VJRD4KICAgIDx0cmFuc2FjdGlvbnM+CiAgICAgICAgPHRyYW5zYWN0aW9uPgogICAgICAgICAgICA8b3JkZXJJRD5jYmJkMzMyN2ExY2Y0ZTE3OGE5ZjM1ZDAyMDZlZmY5OTwvb3JkZXJJRD4KICAgICAgICAgICAgPHJlbW90ZUlEPkE1RzE0NVk3MTg8L3JlbW90ZUlEPgogICAgICAgICAgICA8YW1vdW50PjQuMDA8L2Ftb3VudD4KICAgICAgICAgICAgPGN1cnJlbmN5PlBMTjwvY3VycmVuY3k+CiAgICAgICAgICAgIDxnYXRld2F5SUQ+MzwvZ2F0ZXdheUlEPgogICAgICAgICAgICA8cGF5bWVudERhdGU+MjAyMDExMDMyMjUyNDE8L3BheW1lbnREYXRlPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1cz5TVUNDRVNTPC9wYXltZW50U3RhdHVzPgogICAgICAgICAgICA8cGF5bWVudFN0YXR1c0RldGFpbHM+QVVUSE9SSVpFRDwvcGF5bWVudFN0YXR1c0RldGFpbHM+CiAgICAgICAgICAgIDxzdGFydEFtb3VudD4xLjAwPC9zdGFydEFtb3VudD4KICAgICAgICA8L3RyYW5zYWN0aW9uPgogICAgPC90cmFuc2FjdGlvbnM+CiAgICA8aGFzaD4yM2YwNWZiNTY4MDU4ZjE4OTgzZjFiYjA0YzVjYTY5NjdkZjMxYzhkM2YyYzU1NDllZGVmNmIyZWJmN2Q1ZjJmPC9oYXNoPgo8L3RyYW5zYWN0aW9uTGlzdD4K";
        System.out.println(new String((Base64.getDecoder().decode(baseMessage.getBytes()))));
    }

}