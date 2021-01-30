package com.surgery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "visit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VISIT_SEQ")
    @SequenceGenerator(name = "VISIT_SEQ", allocationSize = 1, sequenceName = "VISIT_SEQ")
    @Column(name = "visit_id")
    private Long visitId;

    @Column(name = "visit_date")
    private Date visitDate;

    @Column(name = "visit_time")
    private String visitTime;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "doctor_first_name")
    private String doctorFirstName;

    @Column(name = "doctor_last_name")
    private String doctorLastName;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "patient_first_name")
    private String patientFirstName;

    @Column(name = "patient_last_name")
    private String patientLastName;

    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "prescribed_medications")
    private String prescribedMedications;

    @Column(name = "taking_medications")
    private String takingMedications;

    @Column(name = "recommendations")
    private String recommendations;
}
