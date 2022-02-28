package fr.miage.filestore.api.resource;

import fr.miage.filestore.api.dto.FileStoreStatus;
import fr.miage.filestore.api.filter.OnlyOwner;
import fr.miage.filestore.api.template.Template;
import fr.miage.filestore.api.template.TemplateContent;
import fr.miage.filestore.auth.AuthenticationService;
import fr.miage.filestore.file.FileServiceException;
import fr.miage.filestore.metrics.MetricsService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("status")
@OnlyOwner
public class StatusResource {

    private static final Logger LOGGER = Logger.getLogger(StatusResource.class.getName());


    @EJB
    private MetricsService metrics;

    @EJB
    private AuthenticationService auth;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FileStoreStatus getStatusJson() throws FileServiceException {
        LOGGER.log(Level.INFO, "GET /api/status");
        return internalGetStatus();
    }

    @GET
    @Template(name = "status")
    @Produces(MediaType.TEXT_HTML)
    public TemplateContent getStatusHtml() throws FileServiceException {
        LOGGER.log(Level.INFO, "GET /api/status (html)");
        TemplateContent<Map<String, Object>> content = new TemplateContent<>();
        Map<String, Object> value = new HashMap<>();
        value.put("profile", auth.getConnectedProfile());
        value.put("status", internalGetStatus());
        content.setContent(value);
        return content;
    }

    private FileStoreStatus internalGetStatus() throws FileServiceException {
        FileStoreStatus status = new FileStoreStatus();
        FileStoreStatus.Server serverStatus = new FileStoreStatus.Server();
        serverStatus.setNbCpus(Runtime.getRuntime().availableProcessors());
        serverStatus.setTotalMemory(Runtime.getRuntime().totalMemory());
        serverStatus.setAvailableMemory(Runtime.getRuntime().freeMemory());
        serverStatus.setMaxMemory(Runtime.getRuntime().maxMemory());
        serverStatus.setUptime(ManagementFactory.getRuntimeMXBean().getUptime());
        status.setServer(serverStatus);

        FileStoreStatus.Store storeStatus = new FileStoreStatus.Store();
        storeStatus.setMetrics(metrics.listMetrics());
        storeStatus.setLatestMetrics(metrics.listLatestMetrics());
        status.setStore(storeStatus);

        return status;
    }


}
