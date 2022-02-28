package fr.miage.filestore.api.filter;

import fr.miage.filestore.auth.AuthenticationService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

    @Context
    private ResourceInfo resourceInfo;

    @EJB
    private AuthenticationService authenticationService;

    @Override
    public void filter(ContainerRequestContext ctx) {
        LOGGER.log(Level.INFO, "Applying security filter");
        if ( resourceInfo.getResourceMethod().isAnnotationPresent(OnlyOwner.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(OnlyOwner.class) ) {
            LOGGER.log(Level.INFO, "Owner annotation present, checking if connected Profile is this store owner");
            if ( !authenticationService.getConnectedProfile().isOwner() ) {
                //TODO if accept is html, use a template for unauthorized
                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }

}
