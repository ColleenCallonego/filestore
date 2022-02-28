package fr.miage.filestore.file;

public class FileItemAlreadyExistsException extends Exception {

    public FileItemAlreadyExistsException(String message) {
        super(message);
    }
}
