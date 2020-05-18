package com.hastlin.zaplacrecepte.model.dto;

import lombok.*;

/**
 * Created by mateuszkaszyk on 17/05/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PrescriptionDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String pesel;
    private String postalCode;
    private String remarks;
    private String phoneNumber;
    private String email;
}
