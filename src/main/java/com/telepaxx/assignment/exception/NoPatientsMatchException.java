package com.telepaxx.assignment.exception;

public class NoPatientsMatchException extends RuntimeException {
    public NoPatientsMatchException(String patientId, String patientLastName) {
        super("No patients matched your search criteria: patientId = %s, patientLastName: %s".formatted(patientId, patientLastName));
    }
}
