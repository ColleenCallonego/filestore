package fr.miage.filestore.store.index;

import java.util.List;

public interface IndexStoreServiceWorker {

    void start();

    void stop();

    List<IndexStoreJob> getQueue();

    void submit(String type, String item);
}
