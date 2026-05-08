package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import com.telepaxx.assignment.roster.RosterLoader;
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


    private final RosterLoader rosterLoader;

    public PatientSearchService(RosterLoader rosterLoader) {
        this.rosterLoader = rosterLoader;
    }

    public PageResponse<PatientResponse> search(PatientSearchCriteria criteria) {
        List<PatientResponse> data = rosterLoader.getAllPatientRecords().stream().map(PatientResponse::from).toList();
        int total = data.size();
        return new PageResponse<>(data, total);
    }

}
