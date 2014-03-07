package org.app.minibank.minibankref1.mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref1.action.CallContext1;
import org.app.minibank.minibankref1.action.IAction1;

import com.fasterxml.jackson.databind.ObjectMapper;

@MessageDriven(name = "MdbFoo1", activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "org/app/minibank/minibankref1/jms/QueueA") })
public class MdbFoo1 implements MessageListener {

    private static final Logger log = Logger.getLogger(MdbFoo1.class);

    @Resource
    private MessageDrivenContext ctx;

    /**
     * The pooled connection factory configured in the server configuration. 
     * Mapped name is set to JNDI name to enable the injection we expect
     */
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactoryNode1;

    @Resource(mappedName = "java:/JmsXA/node_2")
    private ConnectionFactory connectionFactoryNode2;

    @Resource(mappedName = "java:/org/app/minibank/minibankref1/jms/QueueB")
    private Queue queueb;

    @PersistenceContext(unitName = "minibank")
    EntityManager em;

    public void onMessage(Message message) {
        if (!(message instanceof TextMessage)) {
            throw new RuntimeException("message is not a text message but " + message.getClass());
        }
        try {
            String inText = ((TextMessage) message).getText();
            if (log.isDebugEnabled()) log.debug("receive message: " + inText);
            String className = message.getStringProperty("ActionClass");
            @SuppressWarnings("unchecked")
            Class<IAction1> actionClass = (Class<IAction1>) Class.forName(className);
            ObjectMapper mapper = new ObjectMapper();
            IAction1 action = mapper.readValue(inText, actionClass);

            CallContext1 callContext1 = new CallContext1(this, ctx, "onMessage", connectionFactoryNode1, connectionFactoryNode2, queueb, message, em);
            action.doIt(callContext1);

        } catch (Exception e) {
            log.error("" + e, e);
            ctx.setRollbackOnly();
        }

    }
}
