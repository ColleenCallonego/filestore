package fr.miage.filestore.api.mapper;

import fr.miage.filestore.file.FileServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileServiceExceptionMapper implements ExceptionMapper<FileServiceException> {

    @Override
    public Response toResponse(FileServiceException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
}