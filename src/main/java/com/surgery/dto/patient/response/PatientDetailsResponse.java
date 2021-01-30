package com.surgery.dto.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDetailsResponse {
    private Long patientID;

    private String firstName;

    private String lastName;

    private String city;

    private String street;

    private String state;

    private String pesel;

    private Date dateOfBirth;

    private String phone;

    private String email;
}
