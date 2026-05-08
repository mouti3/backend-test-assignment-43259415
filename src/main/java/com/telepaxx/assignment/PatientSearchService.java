package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import com.telepaxx.assignment.exception.MissingCriteriaException;
import com.telepaxx.assignment.exception.NoPatientsMatchException;
import com.telepaxx.assignment.model.PatientRecord;
import com.telepaxx.assignment.roster.RosterLoader;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;

/**
 * Searches patient records loaded from the roster source.
 * <p>
 * TODO: Implement this class.
 * <p>
 * Given search criteria (patientId, lastName, or both), return all matching PatientRecords.
 * <p>
 * Important implementation decisions should be documented in NOTES.md.
 */
@ApplicationScoped
public class PatientSearchService {

    private static final Logger LOG = Logger.getLogger(PatientSearchService.class);

    private final RosterLoader rosterLoader;

    public PatientSearchService(RosterLoader rosterLoader) {
        this.rosterLoader = rosterLoader;
    }

    public PageResponse<PatientResponse> search(PatientSearchCriteria criteria) {
        if (!isValid(criteria.patientId()) && !isValid(criteria.lastName())) {
            throw new MissingCriteriaException("Provide a least: PatientId or PatientLastName");
        }
        List<PatientRecord> allPatientRecords = rosterLoader.getAllPatientRecords();
        int total = allPatientRecords.size();

        List<PatientResponse> data = allPatientRecords.stream()
                .filter(patient -> matchesValue(patient.patientId(), criteria.patientId()))
                .filter(patient -> matchesValue(patient.lastName(), criteria.lastName()))
                .map(PatientResponse::from).toList();
        if (data.isEmpty()) {
            throw new NoPatientsMatchException(criteria.patientId(), criteria.lastName());
        }
        return new PageResponse<>(data, total);
    }

    private boolean matchesValue(String val1, String val2) {
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
