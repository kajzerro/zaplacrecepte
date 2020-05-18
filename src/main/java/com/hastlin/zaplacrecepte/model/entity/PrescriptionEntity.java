package com.hastlin.zaplacrecepte.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "PRESCRIPTION")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class PrescriptionEntity {


    private String id;

    private String firstName;
    private String lastName;
    private String pesel;
    private String postalCode;
    private String remarks;
    private String phoneNumber;
    private String email;


    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getFirstName() {
        return firstName;
    }

    @DynamoDBAttribute
    public String getLastName() {
        return lastName;
    }

    @DynamoDBAttribute
    public String getPesel() {
        return pesel;
    }

    @DynamoDBAttribute
    public String getPostalCode() {
        return postalCode;
    }

    @DynamoDBAttribute
    public String getRemarks() {
        return remarks;
    }

    @DynamoDBAttribute
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @DynamoDBAttribute
    public String getEmail() {
        return email;
    }
}
