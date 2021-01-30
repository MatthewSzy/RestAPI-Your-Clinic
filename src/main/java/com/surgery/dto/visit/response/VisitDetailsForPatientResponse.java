package com.surgery.dto.visit.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDetailsForPatientResponse {
    private Long visitId;

    private String visitDate;

    private String visitTime;

    private String visitType;

    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    private String symptoms;

    private String diagnosis;

    private String prescribedMedications;

    private String takingMedications;

    private String recommendations;
}
