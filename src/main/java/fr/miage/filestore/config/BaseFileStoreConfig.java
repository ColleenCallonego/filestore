package fr.miage.filestore.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Default
@Named("config")
@ApplicationScoped
public class BaseFileStoreConfig implements FileStoreConfig {

    private static final Logger LOGGER = Logger.getLogger(BaseFileStoreConfig.class.getName());
    private static final Map<String, String> CONFIG = new HashMap<>();
    static {
        CONFIG.put("home", System.getProperty("user.home") + File.separatorChar + ".filestore");
        CONFIG.put("owner", "tonka");
        CONFIG.put("consulHttps", "false");
        CONFIG.put("consulHost", "localhost");
        CONFIG.put("consulPort", "8500");
        CONFIG.put("corsOrigin", "*");
        CONFIG.put("corsMethods", "GET, POST, PUT, DELETE, OPTIONS");
        CONFIG.put("corsMaxAge", "-1");
        CONFIG.put("corsHeaders", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Location");
        CONFIG.put("corsExposeHeaders", "Location");
        CONFIG.put("autoMigrate", "true");
    }

    private Path home = null;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initialising config");
        for(String key: CONFIG.keySet()) {
            if (getConfigValue(key) != null) {
                CONFIG.put(key, getConfigValue(key));
            }
        }
        home = Paths.get(CONFIG.get("home"));
        LOGGER.log(Level.INFO, "Config values loaded: " + CONFIG);
    }

    @Override
    public Path getHome() {
        return home;
    }

    @Override
    public String getOwner() {
        return CONFIG.get("owner");
    }

    @Override
    public boolean consultHttps() {
        return Boolean.valueOf(CONFIG.get("consulHttps"));
    }

    @Override
    public String consulHost() {
        return CONFIG.get("consulHost");
    }

    @Override
    public int consulPort() {
        return Integer.valueOf(CONFIG.get("consulPort"));
    }

    @Override
    public String corsOrigin() {
        return CONFIG.get("corsOrigin");
    }

    @Override
    public String corsMethods() {
        return CONFIG.get("corsMethods");
    }

    @Override
    public String corsMaxAge() {
        return CONFIG.get("corsMaxAge");
    }

    @Override
    public String corsHeaders() {
        return CONFIG.get("corsHeaders");
    }

    @Override
    public String corsExposeHeader() {
        return CONFIG.get("corsExposeHeaders");
    }

    private String getConfigValue(String key) {
        if ( System.getenv(getEnvName(key)) != null ) {
            return System.getenv(getEnvName(key));
        } else if (System.getProperty(getPropertyName(key)) != null) {
            return System.getProperty(getPropertyName(key));
        } else {
            return null;
        }
    }

    private String getEnvName(String param) {
        return "FILESTORE_".concat(param.toUpperCase(Locale.ROOT));
    }

    private String getPropertyName(String param) {
        return "filestore".concat(param.substring(0,1).toUpperCase(Locale.ROOT)).concat(param.substring(1).toLowerCase(Locale.ROOT));
    }

}

