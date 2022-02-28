package fr.miage.filestore.neighbourhood.entity;

import com.orbitz.consul.model.health.ServiceHealth;
import io.smallrye.common.constraint.NotNull;

public class Neighbour {

    private String id;
    private String name;
    private String address;
    private String fqdn;

    public Neighbour() {
    }

    public Neighbour(String id, String name, String address, String fqdn) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.fqdn = fqdn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public static Neighbour build(@NotNull ServiceHealth health) {
        Neighbour neighbour = new Neighbour(health.getService().getId(), health.getService().getService().substring("filestore.".length()), health.getService().getAddress() + ":" + health.getService().getPort(), "");
        //neighbour.status = health.getChecks().stream().filter(check -> !check.getStatus().equals("passing")).findAny().map(check -> check.getStatus()).orElse("passing");
        return neighbour;
    }

    @Override
    public String toString() {
        return "Neighbour{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", fqdn='" + fqdn + '\'' +
                '}';
    }


}
