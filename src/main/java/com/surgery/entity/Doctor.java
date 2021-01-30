package com.surgery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCTOR_SEQ")
    @SequenceGenerator(name = "DOCTOR_SEQ", allocationSize = 1, sequenceName = "DOCTOR_SEQ")
    @Column(name = "doctor_id")
    private Long doctorID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pwz")
    private String pwz;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "state")
    private String state;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private Date dateBirth;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
