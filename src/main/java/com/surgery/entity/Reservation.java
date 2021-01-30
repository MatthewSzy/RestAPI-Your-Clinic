package com.surgery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVATION_SEQ")
    @SequenceGenerator(name = "RESERVATION_SEQ", allocationSize = 1, sequenceName = "RESERVATION_SEQ")
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @Column(name = "reservation_time")
    private String reservationTime;

    @Column(name = "reservation_type")
    private String reservationType;

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

    @Column(name = "meeting_link")
    private String meetingLink;
}
