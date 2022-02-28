package fr.miage.filestore.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("test-config")
@Alternative
@ApplicationScoped
public class TestFileStoreConfig implements  FileStoreConfig {

    private static final Logger LOGGER = Logger.getLogger(TestFileStoreConfig.class.getName());

    private Path home;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initialising config");
        home = Paths.get("/tmp", String.valueOf(System.currentTimeMillis()));
        LOGGER.log(Level.INFO, "Filestore home set to : " + home.toString());
    }

    @Override
    public Path getHome() {
        return home;
    }

    @Override
    public String getOwner() {
        return "test";
    }

    @Override
    public boolean consultHttps() {
        return false;
    }

    @Override
    public String consulHost() {
        return "localhost";
    }

    @Override
    public int consulPort() {
        return 6543;
    }

    @Override
    public String corsOrigin() {
        return "*";
    }

    @Override
    public String corsMethods() {
        return "GET, POST, PUT, DELETE, OPTIONS";
    }

    @Override
    public String corsMaxAge() {
        return "-1";
    }

    @Override
    public String corsHeaders() {
        return "";
    }

    @Override
    public String corsExposeHeader() {
        return "";
    }
}

