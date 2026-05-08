package com.telepaxx.assignment.mapper;

import com.telepaxx.assignment.dto.ErrorResponse;
import com.telepaxx.assignment.exception.NoPatientsMatchException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NoPatientsMatchExceptionMapper implements ExceptionMapper<NoPatientsMatchException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NoPatientsMatchException exception) {
        ErrorResponse body = ErrorResponse.of(
                404,
                "Not Found",
                exception.getMessage(),
                uriInfo.getPath()
                );
        return Response.status(Response.Status.NOT_FOUND).entity(body).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
