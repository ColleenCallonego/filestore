package fr.miage.filestore.api.filter;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;


@Provider
@Priority(Priorities.ENTITY_CODER)
public class EncodingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        context.setProperty(InputPart.DEFAULT_CHARSET_PROPERTY, "UTF-8");
    }
} 