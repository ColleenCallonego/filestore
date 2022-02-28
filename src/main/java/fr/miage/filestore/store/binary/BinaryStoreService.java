package fr.miage.filestore.store.binary;

import java.io.InputStream;

public interface BinaryStoreService {

    boolean exists(String key) throws BinaryStoreServiceException;

    String put(InputStream is) throws BinaryStoreServiceException;

    String type(String key, String name) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    long size(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    String extract(String key, String name, String type) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

}
