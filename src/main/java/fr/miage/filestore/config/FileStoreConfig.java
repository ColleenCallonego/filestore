package fr.miage.filestore.config;

import java.nio.file.Path;

public interface FileStoreConfig {

    Path getHome();

    String getOwner();

    // Neighbourhood

    boolean consultHttps();

    String consulHost();

    int consulPort();

    // Cors

    String corsOrigin();

    String corsMethods();

    String corsMaxAge();

    String corsHeaders();

    String corsExposeHeader();

}
