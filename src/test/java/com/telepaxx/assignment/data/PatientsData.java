package com.telepaxx.assignment.data;

import com.telepaxx.assignment.model.PatientRecord;

import java.util.List;

public class PatientsData {
    public final static String PATIENT_ID = "P2004";
    public final static String PATIENT_NAME = "Weiss";
    public final static String API_PATH = "/api/v1/patients/search";
    public final static String MISSING_CRITERIA_MESSAGE = "Provide at least one of: PatientId or PatientLastName";
    public final static String NO_PATIENTS_MATCH = "No patients matched your search criteria: patientId = null, patientLastName: John";

    //PatientRecords data
    public final static List<PatientRecord> PATIENTS_RECORDS = List.of(
            new PatientRecord("P2004", "Weiss", "Alice", "alice.dcm"),
            new PatientRecord("P2005", "Smith", "Bob", "bob.dcm"),
            new PatientRecord("P2006", "Jones", "Carol", "carol.dcm"),
            new PatientRecord(null, null, null, "broken.dcm")
    );
}
