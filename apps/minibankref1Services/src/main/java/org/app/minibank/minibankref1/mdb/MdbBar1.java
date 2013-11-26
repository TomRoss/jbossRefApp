package org.app.minibank.minibankref1.mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref1.action.CallContext1;
import org.app.minibank.minibankref1.action.IAction1;

import com.fasterxml.jackson.databind.ObjectMapper;

@MessageDriven(name = "MdbBar1", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "org/app/minibank/minibankref1/jms/QueueC"),
        // WARNING: placeholders in annotations will not work! but work inside deployment descriptors .
        @ActivationConfigProperty(propertyName = "connectorClassName", propertyValue = "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory,org.hornetq.core.remoting.impl.netty.NettyConnectorFactory"),
        @ActivationConfigProperty(propertyName = "connectionParameters", propertyValue = "host=devpc016918;port=5349,host=devpc016918;port=5449")
// @ActivationConfigProperty(propertyName = "clientID", propertyValue = "${jboss.node.name}") 
})
public class MdbBar1 implements MessageListener {

    private static final Logger log = Logger.getLogger(MdbBar1.class);

    @Resource
    private MessageDrivenContext ctx;

    /**
     * The pooled connection factory configured in the server configuration. 
     * Mapped name is set to JNDI name to enable the injection we expect
     */
    @Resource(mappedName = "java:/RemoteJmsXA")
    private ConnectionFactory connectionFactory;

    //    @Resource(mappedName = "java:/org/app/minibank/minibankref1/jms/QueueD")
    //    private Queue queueD;

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

            CallContext1 callContext1 = new CallContext1(this, ctx, "onMessage", connectionFactory, null);
            action.doIt(callContext1);

        } catch (Exception e) {
            log.error("" + e, e);
            ctx.setRollbackOnly();
        }

    }
}