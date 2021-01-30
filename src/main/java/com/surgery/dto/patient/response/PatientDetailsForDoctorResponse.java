package com.surgery.dto.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDetailsForDoctorResponse {
    private Long patientId;

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
