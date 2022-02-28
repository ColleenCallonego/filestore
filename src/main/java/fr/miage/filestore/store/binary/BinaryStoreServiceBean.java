package fr.miage.filestore.store.binary;

import fr.miage.filestore.config.FileStoreConfig;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class BinaryStoreServiceBean implements BinaryStoreService {

    private static final Logger LOGGER = Logger.getLogger(BinaryStoreService.class.getName());
    public static final String BINARY_DATA_HOME = "binarydata";

    @Inject
    private FileStoreConfig config;

    private Path base;
    private TikaConfig tika;

    public BinaryStoreServiceBean() {
    }

    @PostConstruct
    public void init() {
        this.base = Paths.get(config.getHome().toString(), BINARY_DATA_HOME);
        LOGGER.log(Level.FINEST, "Initializing service with base folder: " + base);
        try {
            Files.createDirectories(base);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to initialize binary store", e);
        }
        try {
            tika =  new TikaConfig();
        } catch (TikaException | IOException e) {
            LOGGER.log(Level.SEVERE, "unable to initialize tika", e);
        }
    }

    @Override
    public boolean exists(String key) throws BinaryStoreServiceException {
        Path file = Paths.get(base.toString(), key);
        return Files.exists(file);
    }

    @Override
    public long size(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream size", e);
        }
    }

    @Override
    public String type(String key, String name) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, name);
            String mimetype = tika.getDetector().detect(TikaInputStream.get(file), metadata).toString();
            return mimetype;
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream size", e);
        }
    }

    @Override
    public String put(InputStream is) throws BinaryStoreServiceException {
        String key = UUID.randomUUID().toString();
        Path file = Paths.get(base.toString(), key);
        if ( Files.exists(file) ) {
            throw new BinaryStoreServiceException("unable to create file, key already exists");
        }
        try {
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream copy", e);
        }
        return key;
    }

    @Override
    public InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            return Files.newInputStream(file, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error while opening stream", e);
        }
    }

    @Override
    public String extract(String key, String name, String type) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.FINE, "Extract text for key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try (InputStream stream = Files.newInputStream(file)) {
            BodyContentHandler handler = new BodyContentHandler();
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, name);
            metadata.set(Metadata.CONTENT_TYPE, type);
            parser.parse(stream, handler, metadata);
            return handler.toString();
        } catch (IOException | SAXException | TikaException e) {
            throw new BinaryStoreServiceException("unexpected error while opening stream", e);
        }
    }

    @Override
    public void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error while deleting stream", e);
        }
    }

}

