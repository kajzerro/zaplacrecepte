package com.hastlin.zaplacrecepte.service.payment.bm;

import com.hastlin.zaplacrecepte.model.dto.payment.bm.BMStatusChangeRequestDecodedDto;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BMStatusPaymentServiceTest {

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
}