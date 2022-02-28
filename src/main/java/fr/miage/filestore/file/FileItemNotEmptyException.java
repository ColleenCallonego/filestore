package fr.miage.filestore.file;

public class FileItemNotEmptyException extends Exception {

    public FileItemNotEmptyException() {
        super();
    }

    public FileItemNotEmptyException(String message) {
        super(message);
    }

    public FileItemNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

}
