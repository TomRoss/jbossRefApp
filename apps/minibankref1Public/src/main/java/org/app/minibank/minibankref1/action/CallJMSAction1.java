package org.app.minibank.minibankref1.action;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallJMSAction1 extends BaseAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    private static final Logger log = Logger.getLogger(CallJMSAction1.class);

    Properties remoteLookupProperties;

    String jndiQueueOut;

    boolean sendToLocalQueue = true;

    @Override
    protected Object doItBasic(CallContext1 cc) throws AccountException {

        Connection connection = null;
        MessageProducer messageProducer = null;
        try {
            ConnectionFactory connectionFactory = sendToLocalQueue ? cc.getConnectionFactoryLocal() : cc.getConnectionFactoryRemote();
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue sentQueue = null;
            if (sendToLocalQueue) {
                sentQueue = cc.getLocalQueue();
            } else {
                // to make this working you need to add in the globa-modules deps:
                // <module name="org.jboss.remote-naming" slot="main"/>
                // <module name="org.hornetq" slot="main"/>
                // otherwise we get: javax.naming.NamingException: JBAS011843: Failed instantiate InitialContextFactory
                // org.jboss.naming.remote.client.InitialContextFactory from classloader ModuleClassLoader for Module
                // "deployment.minibankref1-3.0.0-SNAPSHOT.ear.minibankref1Services-3.0.0-SNAPSHOT.jar:main" from Service Module Loader
                InitialContext context = new InitialContext(remoteLookupProperties);
                sentQueue = (Queue) context.lookup("org/app/minibank/minibankref1/jms/QueueD");

                // another solution that works:
                // sentQueue = (Queue) session.createQueue("org.app.minibank.minibankref1.jms.QueueD");
            }
            messageProducer = session.createProducer(sentQueue);
            connection.start();
            TextMessage messageOut = session.createTextMessage();

            Object result = cc.getResult();
            ObjectMapper mapper = new ObjectMapper();
            String resultJson = mapper.writeValueAsString(result);

            messageOut.setText(resultJson);
            messageOut.setJMSCorrelationID(cc.getMessage().getJMSCorrelationID());
            messageProducer.send(messageOut);

        } catch (JMSException e) {
            throw new RuntimeException("Error when sending JMS message processing: " + e, e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when Processing JSon message: " + e, e);
        } catch (NamingException e) {
            throw new RuntimeException("Error when sending JMS message processing: " + e, e);
        } finally {
            try {
                if (connection != null) connection.close();
                if (messageProducer != null) messageProducer.close();
            } catch (JMSException e) {
                log.error("Cannot close jms connection", e);
            }
        }
        return cc.getResult();
    }

    public Properties getRemoteLookupProperties() {
        return remoteLookupProperties;
    }

    public CallJMSAction1 setRemoteLookupProperties(Properties remoteLookupProperties) {
        this.remoteLookupProperties = remoteLookupProperties;
        return this;
    }

    public String getJndiQueueOut() {
        return jndiQueueOut;
    }

    public CallJMSAction1 setJndiQueueOut(String jndiQueueOut) {
        this.jndiQueueOut = jndiQueueOut;
        return this;
    }

    public boolean isSendToLocalQueue() {
        return sendToLocalQueue;
    }

    public CallJMSAction1 setSendToLocalQueue(boolean sendToLocalQueue) {
        this.sendToLocalQueue = sendToLocalQueue;
        return this;
    }

}
