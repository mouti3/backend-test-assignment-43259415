package com.telepaxx.assignment;

import com.telepaxx.assignment.data.PatientsData;
import com.telepaxx.assignment.dto.PageResponse;
import com.telepaxx.assignment.dto.PatientResponse;
import com.telepaxx.assignment.dto.PatientSearchCriteria;
import com.telepaxx.assignment.exception.MissingCriteriaException;
import com.telepaxx.assignment.exception.NoPatientsMatchException;
import com.telepaxx.assignment.roster.RosterLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientSearchServiceTest {

    private RosterLoader rosterLoader;
    private PatientSearchService service;

    @BeforeEach
    void setUp() {
        rosterLoader = mock(RosterLoader.class);
        when(rosterLoader.getAllPatientRecords()).thenReturn(PatientsData.PATIENTS_RECORDS);
        service = new PatientSearchService(rosterLoader);
    }

    @Test
    void searchByPatientId_returnsMatch() {
        PageResponse<PatientResponse> result = service.search(new PatientSearchCriteria(PatientsData.PATIENT_ID, null));
        assertThat(result.total()).isEqualTo(4);
        assertThat(result.data()).extracting(PatientResponse::patientId).containsExactly(PatientsData.PATIENT_ID);
    }

    @Test
    void searchByPatientLastName_returnsMatch() {
        PageResponse<PatientResponse> result = service.search(new PatientSearchCriteria(null, PatientsData.PATIENT_NAME));
        assertThat(result.total()).isEqualTo(4);
        assertThat(result.data()).extracting(PatientResponse::lastName).containsExactly(PatientsData.PATIENT_NAME);
    }

    @Test
    void searchByEmptyCriteria_returnsNoMatch() {
        assertThatThrownBy(() -> service.search(new PatientSearchCriteria(null, null))).isInstanceOf(MissingCriteriaException.class);
    }

    @Test
    void searchByWrongCriteria_returnsNoMatch() {
        assertThatThrownBy(() -> service.search(new PatientSearchCriteria(null, "Dinho"))).isInstanceOf(NoPatientsMatchException.class);
    }
}
