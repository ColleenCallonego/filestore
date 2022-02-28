package fr.miage.filestore.api.resource;

import fr.miage.filestore.api.filter.OnlyOwner;
import fr.miage.filestore.api.template.Template;
import fr.miage.filestore.api.template.TemplateContent;
import fr.miage.filestore.auth.AuthenticationService;
import fr.miage.filestore.neighbourhood.NeighbourhoodService;
import fr.miage.filestore.neighbourhood.NeighbourhoodServiceException;
import fr.miage.filestore.neighbourhood.entity.Neighbour;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("network")
@OnlyOwner
public class NeighboursResource {

    private static final Logger LOGGER = Logger.getLogger(NeighboursResource.class.getName());

    @EJB
    private NeighbourhoodService neighbourhood;

    @EJB
    private AuthenticationService auth;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Neighbour> getNeighbours() throws NeighbourhoodServiceException {
        LOGGER.log(Level.INFO, "GET /api/neighbours");
        return neighbourhood.list();
    }

    @GET
    @Template(name = "network")
    @Produces(MediaType.TEXT_HTML)
    public TemplateContent getNeighboursHtml() throws NeighbourhoodServiceException {
        LOGGER.log(Level.INFO, "GET /api/neighbours (html)");
        TemplateContent<Map<String, Object>> content = new TemplateContent<>();
        Map<String, Object> value = new HashMap<>();
        value.put("profile", auth.getConnectedProfile());
        value.put("neighbours", neighbourhood.list());
        content.setContent(value);
        return content;
    }
}
