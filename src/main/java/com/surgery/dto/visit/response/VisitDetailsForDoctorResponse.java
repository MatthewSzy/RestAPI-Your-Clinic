package com.surgery.dto.visit.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDetailsForDoctorResponse {
    private Long visitId;

    private String visitDate;

    private String visitTime;

    private String visitType;

    private Long patientId;

    private String patientFirstName;

    private String patientLastName;

    private String symptoms;

    private String diagnosis;

    private String prescribedMedications;

    private String takingMedications;

    private String recommendations;
}
