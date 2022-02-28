package fr.miage.filestore.metrics;

import java.util.Map;

public interface MetricsService {

    Map<String, Long> listMetrics();

    Map<String, Long> listLatestMetrics();

    Long getMetric(String key);

}
