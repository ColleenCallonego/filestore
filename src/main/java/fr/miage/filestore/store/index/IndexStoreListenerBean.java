package fr.miage.filestore.store.index;

import fr.miage.filestore.notification.entity.Event;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(name = "IndexStoreListenerBean", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/notification"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
})
public class IndexStoreListenerBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(IndexStoreListenerBean.class.getName());

    @EJB
    private IndexStoreServiceWorker worker;

    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.log(Level.INFO, "Worker listener message received");
            Event event = Event.fromJMSMessage(message);
            worker.submit(event.getEventType(), event.getSourceId());
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "error while triggering worker", e);
        }
    }


}

