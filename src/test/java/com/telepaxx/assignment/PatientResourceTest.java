package com.telepaxx.assignment;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static com.telepaxx.assignment.data.PatientsData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
public class PatientResourceTest {

    @Test
    void searchByPatientIdAndReturns200() {
        given().queryParam("patientId", PATIENT_ID)
                .when().get(API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON).
                body("total", equalTo(13))
                .body("data.lastName", hasItem(PATIENT_NAME));

    }

    @Test
    void searchByPatientNameAndReturns200() {
        given().queryParam("lastName", PATIENT_NAME)
                .when().get(API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON).
                body("total", equalTo(13))
                .body("data.patientId", hasItem(PATIENT_ID));
    }

    @Test
    void searchByPatientNameAndPatientIdAndReturns200() {
        given().queryParam("patientId", PATIENT_ID).queryParam("lastName", PATIENT_NAME)
                .when().get(API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON).
                body("total", equalTo(13))
                .body("data.patientId", hasItem(PATIENT_ID));
    }

    @Test
    void searchByEmptyLastNameAndReturns400() {
        given().queryParam("lastName", "   ")
                .when().get(API_PATH)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", equalTo(MISSING_CRITERIA_MESSAGE));
    }

    @Test
    void searchByWrongLastNameAndReturns400() {
        given().queryParam("lastName", "John")
                .when().get(API_PATH)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", equalTo(NO_PATIENTS_MATCH));
    }
}
