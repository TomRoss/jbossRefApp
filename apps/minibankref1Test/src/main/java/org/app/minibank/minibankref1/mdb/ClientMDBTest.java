package org.app.minibank.minibankref1.mdb;

import java.util.Enumeration;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMSAction1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientMDBTest {

    private static final Logger log = Logger.getLogger(ClientMDBTest.class);

    EAP6NamingContext ctx1;

    EAP6NamingContext ctx2;

    @Before
    public void before() throws NamingException {
        ctx1 = new EAP6NamingContext(TestUtil.createJmsProperties1());
        ctx2 = new EAP6NamingContext(TestUtil.createJmsProperties2());
    }

    @After
    public void after() throws NamingException {
        if (ctx1 != null) ctx1.close();
        if (ctx2 != null) ctx2.close();
    }

    @Test
    public void sendReceiveMessagesWithLocalQueues() throws Exception {
        sendReceiveMessages(new CallJMSAction1(), ctx1, "org/app/minibank/minibankref1/jms/QueueA", "org/app/minibank/minibankref1/jms/QueueB", 10, 1);
    }

    @Test
    public void sendReceiveMessagesWithLocalQueuesError() throws Exception {
        CallJMSAction1 callJMSAction1 = new CallJMSAction1();
        callJMSAction1.setThrowCheckedException(true);
        sendReceiveMessages(callJMSAction1, ctx1, "org/app/minibank/minibankref1/jms/QueueA", "org/app/minibank/minibankref1/jms/QueueB", 1, 1);
    }

    @Test
    public void sendReceiveMessagesWithRemoteQueues() throws Exception {
        CallJMSAction1 callJMSAction1 = new CallJMSAction1();
        callJMSAction1.setSendToLocalQueue(false);
        callJMSAction1.setRemoteLookupProperties(TestUtil.createJmsProperties2());
        sendReceiveMessages(callJMSAction1, ctx2, "org/app/minibank/minibankref1/jms/QueueC", "org/app/minibank/minibankref1/jms/QueueD", 1, 1);
    }

    @SuppressWarnings("unchecked")
    public void sendReceiveMessages(CallJMSAction1 action, EAP6NamingContext customCtx, String jndiQueueProducer, String jndiQueueConsumer, int nbMessages,
            int displayMessagesEvery) throws Exception {
        Connection connection = null;
        Session session = null;
        MessageProducer msgProducer = null;
        MessageConsumer msgConsumer = null;
        try {
            ConnectionFactory cf = (ConnectionFactory) customCtx.lookup("jms/RemoteConnectionFactory");
            Destination destinationProducer = (Destination) customCtx.lookup(jndiQueueProducer);
            Destination destinationConsumer = (Destination) customCtx.lookup(jndiQueueConsumer);
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            msgProducer = session.createProducer(destinationProducer);
            // must create consumer before sending messages otherwise messages could be sent to the queue with no consumer in the cluster.
            msgConsumer = session.createConsumer(destinationConsumer);
            connection.start();

            ObjectMapper mapper = new ObjectMapper();
            action.setDisplaylogEvery(displayMessagesEvery);

            for (int i = 0; i < nbMessages; i++) {
                action.setCallId(i);
                String jSonString = mapper.writeValueAsString(action);
                TextMessage msg = session.createTextMessage(jSonString);
                msg.setStringProperty("ActionClass", action.getClass().getName());
                msgProducer.send(msg);
                if (i % displayMessagesEvery == 0) log.info("Message " + i + " sent with jmsMessageID=" + msg.getJMSMessageID() + " : " + jSonString);
            }

            int nbMessageRead = 0;
            while (nbMessageRead != 0) {
                Message m = msgConsumer.receive(2000);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        TextMessage message = (TextMessage) m;
                        String text = message.getText();
                        Map<String, Object> results = mapper.readValue(text, Map.class);
                        if (nbMessageRead % displayMessagesEvery == 0) log.info("Reading message: " + results);

                    } else {
                        log.warn("Reading message: Not a Text message");
                    }
                    nbMessageRead++;
                } else {
                    break;
                }
            }
            Assert.assertEquals(nbMessages, nbMessageRead);

        } finally {
            if (msgProducer != null) msgProducer.close();
            if (msgConsumer != null) msgConsumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void browseRemoteQueueOnNode2Test() throws Exception {
        Connection connection = null;
        Session session = null;
        MessageProducer msgProducer = null;
        long start = System.currentTimeMillis();
        try {

            ConnectionFactory cf = (ConnectionFactory) ctx2.lookup("jms/RemoteConnectionFactory");
            Destination destination = (Destination) ctx2.lookup("org/app/minibank/minibankref1/jms/QueueD");
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            msgProducer = session.createProducer(destination);

            QueueBrowser qb = session.createBrowser((Queue) destination);
            int msgCounter = 0;
            Enumeration<Message> queueEnum = qb.getEnumeration();
            while (queueEnum.hasMoreElements()) {
                Message msg = (Message) queueEnum.nextElement();
                if (!(msg instanceof TextMessage)) {
                    throw new RuntimeException("message is not a text message but " + msg.getClass());
                }
                String txtMsg = ((TextMessage) msg).getText();
                log.info("Queue contains Message with: " + txtMsg);
                msgCounter++;
            }
            log.info("In total the queue now contains " + msgCounter + " Messages to process");
        } finally {
            if (msgProducer != null) msgProducer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }

        long end = System.currentTimeMillis();
        log.info("Queue browsed in " + (end - start) + " ms");

    }
}
