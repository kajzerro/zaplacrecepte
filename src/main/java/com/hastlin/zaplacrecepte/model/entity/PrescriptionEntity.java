package com.hastlin.zaplacrecepte.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRESCRIPTION")
public class PrescriptionEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PESEL")
    private String pesel;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "REMARKS")
    @Type(type = "text")
    private String remarks;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_DATETIME")
    private ZonedDateTime createDateTime;

    @Column(name = "ORDER_ID")
    private int orderId;

    @Column(name = "PAYMENT_TOKEN")
    private String paymentToken;

    @Column(name = "ORDER_URL")
    private String orderUrl;

    @Column(name = "PRESCRIPTION_NUMBER")
    private String prescriptionNumber;

    @Column(name = "ERRORS")
    @Type(type = "text")
    private String errors;

    public void addError(String newError) {
        if (Objects.isNull(errors)) {
            errors = "";
        }
        this.errors += newError + ";";
    }

}
