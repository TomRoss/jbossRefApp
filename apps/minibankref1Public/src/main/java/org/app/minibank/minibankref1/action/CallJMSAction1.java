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

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;

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
            ConnectionFactory connectionFactory = cc.getConnectionFactory();
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queueD = null;
            if (sendToLocalQueue) {
                queueD = cc.getLocalQueue();
            } else {
                // we can't inject a destination, because the remote queue are <b>not</b> avialable in the local JNDI. 
                // the only chance we have is to create the producer from the session.
                // So we cannot use the JNDI name
                // we could use:
                //queueD = session.createQueue("org.app.minibank.minibankref1.jms.QueueD");

                // but as the queue is remote we need to do:
                InitialContext context = new InitialContext(remoteLookupProperties);
                queueD = (Queue) context.lookup("org/app/minibank/minibankref1/jms/QueueD");
            }
            messageProducer = session.createProducer(queueD);
            connection.start();
            TextMessage messageOut = session.createTextMessage();

            Object result = cc.getResult();
            ObjectMapper mapper = new ObjectMapper();
            String resultJson = mapper.writeValueAsString(result);

            messageOut.setText(resultJson);
            messageProducer.send(messageOut);

        } catch (Exception e) {
            log.error("" + e, e);
            cc.getEjbContext().setRollbackOnly();
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
