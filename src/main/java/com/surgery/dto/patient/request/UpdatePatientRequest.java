package com.surgery.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePatientRequest {
    private String firstName;

    private String lastName;

    private String city;

    private String street;

    private String state;

    private String phone;

    private String email;
}
