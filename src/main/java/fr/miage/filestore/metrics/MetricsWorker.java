package fr.miage.filestore.metrics;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MetricsWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(MetricsWorker.class.getName());

    private MetricsService metrics;

    public MetricsWorker(MetricsService metrics) {
        LOGGER.log(Level.INFO, "Instanciate MetricsWorker");
        this.metrics = metrics;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "MetricsWorker is running");
        LOGGER.log(Level.INFO, "Latest metrics: " +  metrics.listLatestMetrics());
        LOGGER.log(Level.INFO, "MetricsWorker has worked");
    }
}
