package com.hastlin.zaplacrecepte.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class UserEntity {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CLIENT_TYPE")
    private String clientType;

    @Column(name = "DEFAULT_PRICE")
    private Integer defaultPrice;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SMS_MESSAGE_REQUEST_PAYMENT")
    private String smsMessageRequestPayment;

    @Column(name = "SMS_MESSAGE_COMPLETED")
    private String smsMessageCompleted;

    @Column(name = "PAYMENT_PROVIDER")
    private String paymentProvider;

    @Column(name = "CREATE_DATETIME")
    private ZonedDateTime createDateTime;

    @Column(name = "MODIFICATION_DATETIME")
    private ZonedDateTime modificationDateTime;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_OWNER")
    private String accountOwner;

    @Column(name = "FEE_INCLUDED")
    private boolean feeIncluded;

}
