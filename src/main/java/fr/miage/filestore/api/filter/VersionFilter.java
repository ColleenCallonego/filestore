package fr.miage.filestore.api.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class VersionFilter implements ContainerResponseFilter {

    private static final String VERSION = "1.0.0-SNAPSHOT";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("filestore-version", VERSION);
    }
}
