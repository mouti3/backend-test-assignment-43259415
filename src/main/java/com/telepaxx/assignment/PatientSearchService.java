package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import com.telepaxx.assignment.exception.MissingCriteriaException;
import com.telepaxx.assignment.exception.NoPatientsMatchException;
import com.telepaxx.assignment.model.PatientRecord;
import com.telepaxx.assignment.roster.RosterLoader;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PatientSearchService {

    private final RosterLoader rosterLoader;

    public PatientSearchService(RosterLoader rosterLoader) {
        this.rosterLoader = rosterLoader;
    }

    public PageResponse<PatientResponse> search(PatientSearchCriteria criteria) {
        if (!isValid(criteria.patientId()) && !isValid(criteria.lastName())) {
            throw new MissingCriteriaException("Provide at least one of: PatientId or PatientLastName");
        }
        List<PatientRecord> allPatientRecords = rosterLoader.getAllPatientRecords();
        int total = allPatientRecords.size();

        List<PatientResponse> data = allPatientRecords.stream()
                .filter(patient -> matchesCriterion(patient.patientId(), criteria.patientId()))
                .filter(patient -> matchesCriterion(patient.lastName(), criteria.lastName()))
                .map(PatientResponse::from).toList();
        if (data.isEmpty()) {
            throw new NoPatientsMatchException(criteria.patientId(), criteria.lastName());
        }
        return new PageResponse<>(data, total);
    }

    private boolean matchesCriterion(String val1, String val2) {
        if (val2 == null || val2.trim().isEmpty())
            return true;
        return val1 != null && val1.equalsIgnoreCase(val2);
    }

    private boolean isValid(String value) {
        if (value == null)
            return false;
        return !value.trim().isEmpty();
    }

}
