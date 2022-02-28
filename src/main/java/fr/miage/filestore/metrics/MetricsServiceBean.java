package fr.miage.filestore.metrics;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class MetricsServiceBean implements MetricsService {

    private static final Logger LOGGER = Logger.getLogger(MetricsService.class.getName());

    private static Map<String, Long> metrics = new HashMap<>();
    private static Map<String, Long> latestMetrics = new HashMap<>();

    @Resource(lookup = "java:jboss/ee/concurrency/scheduler/default")
    private ManagedScheduledExecutorService executorService;

    public MetricsServiceBean() {
    }

    @PostConstruct
    private void init() {
        LOGGER.log(Level.INFO, "Initializing MetricsService");
        MetricsWorker worker = new MetricsWorker(this);
        executorService.scheduleAtFixedRate(worker,1, 30, TimeUnit.MINUTES);
    }

    @Override
    public Map<String, Long> listMetrics() {
        LOGGER.log(Level.INFO, "List all metrics");
        return metrics;
    }

    @Override
    public Map<String, Long> listLatestMetrics() {
        LOGGER.log(Level.INFO, "List latest metrics");
        return latestMetrics;
    }

    @Override
    public Long getMetric(String key) {
        LOGGER.log(Level.INFO, "get metric for key: " + key);
        return metrics.getOrDefault(key, 0l);
    }

    @Schedule(hour = "1", persistent = false)
    private void razLatestMetrics() {
        LOGGER.log(Level.INFO, "reset latests metrics");
        for(String key: latestMetrics.keySet()) {
            latestMetrics.put(key, 0l);
        }
    }

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        LOGGER.log(Level.INFO, "Entering interceptor or method: " + ic.getTarget().toString(), ic.getMethod().getName());
        try {
            Object obj =  ic.proceed();
            if ( ic.getMethod().isAnnotationPresent(Metrics.class) ) {
                LOGGER.log(Level.INFO, "Annotation metrics found, applying metrics");
                Metrics annotation = ic.getMethod().getAnnotation(Metrics.class);
                Long value = metrics.getOrDefault(annotation.key(), 0l);
                switch ( annotation.type() ) {
                    case INCREMENT: value++; break;
                    case DECREMENT: value--; break;
                }
                metrics.put(annotation.key(), value);
                latestMetrics.put(annotation.key(), value);
            }
            return obj;
        } finally {
            LOGGER.log(Level.INFO, "Exiting interceptor or method: " + ic.getTarget().toString(), ic.getMethod().getName());
        }
    }

}
