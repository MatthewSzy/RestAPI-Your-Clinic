package com.surgery.dto.doctor.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogInDoctorRequest {
    private String email;

    private String password;
}
