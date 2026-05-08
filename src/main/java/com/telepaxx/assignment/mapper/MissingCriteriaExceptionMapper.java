package com.telepaxx.assignment.mapper;

import com.telepaxx.assignment.dto.ErrorResponse;
import com.telepaxx.assignment.exception.MissingCriteriaException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MissingCriteriaExceptionMapper implements ExceptionMapper<MissingCriteriaException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(MissingCriteriaException exception) {
        ErrorResponse body = ErrorResponse.of(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Bad Request",
                exception.getMessage(),
                uriInfo.getPath()
        );
        return Response.status(
                Response.Status.BAD_REQUEST
        ).entity(body).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
