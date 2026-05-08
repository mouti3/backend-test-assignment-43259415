package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Searches patient records loaded from the roster source.
 *
 * TODO: Implement this class.
 *
 * Given search criteria (patientId, lastName, or both), return all matching PatientRecords.
 *
 * Important implementation decisions should be documented in NOTES.md.
 */
@ApplicationScoped
public class PatientSearchService {

    // TODO: inject RosterLoader and implement search
    public PageResponse<PatientResponse> search(PatientSearchCriteria criteria) {
        return new PageResponse<>(List.of(), 0);
    }

}
