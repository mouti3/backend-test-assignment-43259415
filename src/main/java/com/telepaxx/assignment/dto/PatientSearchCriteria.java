package com.telepaxx.assignment.dto;

import jakarta.ws.rs.QueryParam;

public record PatientSearchCriteria(
        @QueryParam("patientId") String patientId,
        @QueryParam("lastName") String lastName
) {
}
