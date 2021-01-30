package com.surgery.dto.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLinkRequest {
    private Long reservationId;

    private String meetingLink;
}
