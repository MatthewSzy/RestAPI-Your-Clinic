package com.surgery.dto.reservation.request;

import com.surgery.enumeration.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReservationRequest {
    private String reservationDate;

    private String reservationTime;

    private ReservationType reservationType;

    private Long doctorId;

    private Long patientId;
}
