package com.surgery.dto.visit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVisitRequest {

    private String symptoms;

    private String diagnosis;

    private String prescribedMedications;

    private String takingMedications;

    private String recommendations;
}
