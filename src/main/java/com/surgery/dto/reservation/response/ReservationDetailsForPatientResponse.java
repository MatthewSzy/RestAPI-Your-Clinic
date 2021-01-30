package com.surgery.dto.reservation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDetailsForPatientResponse {
    private Long reservationId;

    private Date reservationDate;

    private String reservationTime;

    private String reservationType;

    private Long doctorId;

    private String firstName;

    private String lastName;

    private String meetingLink;
}
