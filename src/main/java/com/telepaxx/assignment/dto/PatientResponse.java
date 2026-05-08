package com.telepaxx.assignment.dto;

import com.telepaxx.assignment.model.PatientRecord;

public record PatientResponse(
        String patientId,
        String lastName,
        String firstName,
        String fileName
) {
    public static PatientResponse from(PatientRecord p) {
        return new PatientResponse(p.patientId(), p.lastName(), p.firstName(), p.fileName());
    }
}
