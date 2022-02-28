package fr.miage.filestore.mock;

import fr.miage.filestore.store.binary.BinaryStoreService;
import fr.miage.filestore.store.binary.BinaryStoreServiceException;
import fr.miage.filestore.store.binary.BinaryStreamNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class MockedMemoryBinaryStore implements BinaryStoreService {

    private static final Logger LOGGER = Logger.getLogger(MockedMemoryBinaryStore.class.getName());

    private final Map<String, byte[]> store = new HashMap<>();
    private TikaConfig tika;

    @PostConstruct
    public void init() {
        try {
            tika =  new TikaConfig();
        } catch (TikaException | IOException e) {
            LOGGER.log(Level.SEVERE, "unable to initialize tika", e);
        }
    }

    @Override
    public boolean exists(String key) throws BinaryStoreServiceException {
        LOGGER.log(Level.INFO, "Is exists key: " + key);
        return store.containsKey(key);
    }

    @Override
    public String type(String key, String name) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.INFO, "Get type for key: " + key);
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, name);
            InputStream is = TikaInputStream.get(get(key));
            String mimetype = tika.getDetector().detect(is, metadata).toString();
            is.close();
            return mimetype;
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream size", e);
        }
    }

    @Override
    public String put(InputStream is) throws BinaryStoreServiceException {
        LOGGER.log(Level.INFO, "Putting content");
        String key = UUID.randomUUID().toString();
        try {
            byte[] content = IOUtils.toByteArray(is);
            store.put(key, content);
            is.close();
            LOGGER.log(Level.INFO, new String(content));
            return key;
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unable to put stream in store", e);
        }
    }

    @Override
    public long size(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        if ( store.containsKey(key) ) {
            return store.get(key).length;
        }
        throw new BinaryStreamNotFoundException(key);
    }

    @Override
    public InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        if ( store.containsKey(key) ) {
            return new ByteArrayInputStream(store.get(key));
        }
        throw new BinaryStreamNotFoundException(key);
    }

    @Override
    public String extract(String key, String name, String type) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        return "This is a mocked plain text";
    }

    @Override
    public void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        if ( !store.containsKey(key) ) {
            throw new BinaryStreamNotFoundException(key);
        }
        store.remove(key);
    }
}