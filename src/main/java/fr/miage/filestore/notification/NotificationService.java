package fr.miage.filestore.notification;

public interface NotificationService {

    void throwEvent(String type, String sourceId) throws NotificationServiceException;

}
