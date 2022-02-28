package fr.miage.filestore.store.binary;

public class BinaryStreamNotFoundException extends Exception {

    public BinaryStreamNotFoundException(String message) {
        super(message);
    }

    public BinaryStreamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
