package fr.miage.filestore.api.mapper;

import fr.miage.filestore.file.FileItemAlreadyExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileItemAlreadyExistsExceptionMapper implements ExceptionMapper<FileItemAlreadyExistsException> {

    @Override
    public Response toResponse(FileItemAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}