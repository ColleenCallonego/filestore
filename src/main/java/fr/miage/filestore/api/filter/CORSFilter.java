package fr.miage.filestore.api.filter;

import fr.miage.filestore.config.FileStoreConfig;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Inject
    private FileStoreConfig config;


    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", config.corsOrigin());
        responseContext.getHeaders().add("Access-Control-Allow-Methods", config.corsMethods());
        responseContext.getHeaders().add("Access-Control-Max-Age", config.corsMaxAge());
        responseContext.getHeaders().add("Access-Control-Allow-Headers", config.corsHeaders());
        responseContext.getHeaders().add("Access-Control-Expose-Headers", config.corsExposeHeader());
    }

}
