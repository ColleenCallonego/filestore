package fr.miage.filestore.notification.listener;

import fr.miage.filestore.notification.entity.Event;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(name = "LoggerListener", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/notification"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
})
public class LoggerEventListener implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(LoggerEventListener.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            Event event = Event.fromJMSMessage(message);
            LOGGER.log(Level.INFO, "Event received: " + event.toString());
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "error while receiving event", e);
        }
    }

}