package com.telepaxx.assignment;

import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/patients")
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private final PatientSearchService patientSearchService;

    public PatientResource(PatientSearchService patientSearchService) {
        this.patientSearchService = patientSearchService;
    }

    @GET
    @Path("/search")
    public PageResponse<PatientResponse> search(@BeanParam PatientSearchCriteria criteria) {
        return patientSearchService.search(criteria);
    }

}
