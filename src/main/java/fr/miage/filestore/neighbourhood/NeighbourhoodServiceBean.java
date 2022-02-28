package fr.miage.filestore.neighbourhood;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import fr.miage.filestore.config.FileStoreConfig;
import fr.miage.filestore.neighbourhood.entity.Neighbour;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
@Startup
public class NeighbourhoodServiceBean implements NeighbourhoodService{

    private static final Logger LOGGER = Logger.getLogger(NeighbourhoodService.class.getName());

    @Inject
    private FileStoreConfig config;

    private volatile Consul consulClient;
    private volatile String serviceName;
    private volatile String instanceId;
    private volatile boolean registered = false;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing neighbourhood bean");
        consulClient = Consul.builder().withHttps(config.consultHttps()).withHostAndPort(HostAndPort.fromParts(config.consulHost(), config.consulPort())).build();
        serviceName = "filestore.".concat(config.getOwner());
        instanceId = serviceName.concat(".1");
        Registration service = this.buildRegistration();
        consulClient.agentClient().register(service);
        try {
            consulClient.agentClient().pass(instanceId);
            registered = true;
            LOGGER.log(Level.INFO, "Instance registered with id=" + instanceId);
        } catch (NotRegisteredException e) {
            LOGGER.log(Level.WARNING, "Unable to checkin neighbourhood registration", e);
        }
    }

    @PreDestroy
    public void stop() {
        LOGGER.log(Level.INFO, "Stopping neighbourhood bean");
        if (registered) {
            consulClient.agentClient().deregister(instanceId);
            this.registered = false;
            LOGGER.log(Level.INFO, "Service instance unregistered for id=" + instanceId);
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public List<Neighbour> list() {
        LOGGER.log(Level.INFO, "Listing all neighbours");
        CatalogClient catalog = consulClient.catalogClient();
        Map<String, List<String>> services =  catalog.getServices().getResponse();
        List<String> stores = services.keySet().stream().filter(key -> key.startsWith("filestore.")).collect(Collectors.toList());
        LOGGER.log(Level.INFO, "Found stores: " + stores);
        return stores.stream().flatMap(name -> consulClient.healthClient().getAllServiceInstances(name).getResponse().stream().map(Neighbour::build)).collect(Collectors.toList());
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void checkin() {
        if (registered) {
            LOGGER.log(Level.FINEST, "Checkin filestore in neighbourhood");
            try {
                consulClient.agentClient().pass(instanceId);
            } catch (NotRegisteredException e) {
                LOGGER.log(Level.WARNING, "Error while trying to checkin service in neighbourhood", e);
                Registration service = this.buildRegistration();
                consulClient.agentClient().deregister(instanceId);
                this.registered = false;
                consulClient.agentClient().register(service);
                this.registered = true;
            }
        }
    }

    private Registration buildRegistration() {
        return ImmutableRegistration.builder()
                .id(instanceId)
                .name(serviceName)
                //.address("http://".concat(((appHost.equals("0.0.0.0")) ? "127.0.0.1" : appHost)))
                //.port(appPort)
                .check(ImmutableRegCheck.builder().ttl(String.format("%ss", 30l)).deregisterCriticalServiceAfter(String.format("%sh",1)).build())
                .build();
    }


}
