package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * HTTP endpoint for patient search.
 *
 * TODO: Design and implement the search endpoint.
 *
 * Requirements:
 *   - Accept search criteria: PatientID, last name, or both
 *   - Return a list of matching DICOM files with metadata you consider relevant
 *   - Handle the case where no results are found
 *
 * Important implementation decisions should be documented in NOTES.md.
 *
 * This bootstrap path can be kept or changed if justified.
 */
@Path("/api/v1/patients")
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private final PatientSearchService patientSearchService;

    public PatientResource(PatientSearchService patientSearchService) {
        this.patientSearchService = patientSearchService;
    }

    @GET
    @Path("/search")
    public PageResponse<PatientResponse> search(@Valid @BeanParam PatientSearchCriteria criteria) {
        return patientSearchService.search(criteria);
    }



}
