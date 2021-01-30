package com.surgery.dto.doctor.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorRequest {
    private String firstName;

    private String lastName;

    private String pwz;

    private String specialization;

    private String pesel;

    private String city;

    private String street;

    private String state;

    private String phone;

    private String email;

    private String password;
}