package com.surgery.dto.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationTermRequest {
    private String reservationDate;

    private String reservationTime;

    private Long doctorId;
}
