package com.telepaxx.assignment.mapper;

import com.telepaxx.assignment.dto.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException wae) {
            int status = wae.getResponse().getStatus();
            ErrorResponse body = ErrorResponse.of(status,
                    Response.Status.fromStatusCode(status).getReasonPhrase(),
                    wae.getMessage(), uriInfo.getPath());
            return Response.status(status).entity(body).type(MediaType.APPLICATION_JSON).build();
        }
        ErrorResponse body = ErrorResponse.of(
                500,
                "Internal Server Error",
                exception.getMessage(),
                uriInfo.getPath()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
