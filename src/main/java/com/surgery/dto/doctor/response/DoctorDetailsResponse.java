package com.surgery.dto.doctor.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDetailsResponse {
    private Long doctorId;

    private String firstName;

    private String lastName;

    private String pwz;

    private String specialization;

    private String pesel;

    private Date dateOfBirth;

    private String city;

    private String street;

    private String state;

    private String phone;

    private String email;
}
