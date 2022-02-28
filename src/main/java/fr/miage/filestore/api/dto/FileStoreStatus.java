package fr.miage.filestore.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileStoreStatus {

    private Server server;
    private Store store;

    public FileStoreStatus() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public static class Store {

        private String name;
        private long totalSpace;
        private long usableSpace;
        private boolean readOnly;
        private Map<String, Long> metrics;
        private Map<String, Long> latestMetrics;

        public Store() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTotalSpace() {
            return totalSpace;
        }

        public void setTotalSpace(long totalSpace) {
            this.totalSpace = totalSpace;
        }

        public long getUsableSpace() {
            return usableSpace;
        }

        public void setUsableSpace(long usableSpace) {
            this.usableSpace = usableSpace;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public void setReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
        }

        public Map<String, Long> getMetrics() {
            return metrics;
        }

        public void setMetrics(Map<String, Long> metrics) {
            this.metrics = metrics;
        }

        public Map<String, Long> getLatestMetrics() {
            return latestMetrics;
        }

        public void setLatestMetrics(Map<String, Long> latestMetrics) {
            this.latestMetrics = latestMetrics;
        }
    }

    public static class Server {

        private long totalMemory;
        private long availableMemory;
        private long maxMemory;
        private int nbCpus;
        private long uptime;

        public Server() {
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public void setTotalMemory(long totalMemory) {
            this.totalMemory = totalMemory;
        }

        public long getAvailableMemory() {
            return availableMemory;
        }

        public void setAvailableMemory(long availableMemory) {
            this.availableMemory = availableMemory;
        }

        public long getMaxMemory() {
            return maxMemory;
        }

        public void setMaxMemory(long maxMemory) {
            this.maxMemory = maxMemory;
        }

        public int getNbCpus() {
            return nbCpus;
        }

        public void setNbCpus(int nbCpus) {
            this.nbCpus = nbCpus;
        }

        public long getUptime() {
            return uptime;
        }

        public void setUptime(long uptime) {
            this.uptime = uptime;
        }
    }
}

