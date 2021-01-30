package com.surgery.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {
    private String firstName;

    private String lastName;

    private String city;

    private String street;

    private String state;

    private String pesel;

    private String phone;

    private String email;

    private String password;
}
