package org.app.minibank.minibankref1.mdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.JBNode;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMSAction1;
import org.app.minibank.minibankref1.jmx.ClientJmxRolesTest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JMSTestUtil {

    private static final Logger log = Logger.getLogger(JMSTestUtil.class);

    public static void executeProcess(String testName, String command, JBNode... nodes) throws Exception {
        for (JBNode node : nodes) {
            String nodeScript = node.getPath() + TestUtil.PATH_SEPARATOR + command;
            File fileNode = new File(nodeScript);
            String line = "cmd /C " + fileNode.getName();
            log.info("[" + testName + "] start launching '" + line + "'");
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(fileNode.getParentFile());
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor(5000);
            log.info("[" + testName + "] end launching '" + line + "'");
        }
    }

    public static void pingServer(String testName, JBNode... nodes) throws Exception {
        for (JBNode node : nodes) {
            int nbLoop = 0;
            int maxNbLoop = 10;
            for (int i = 0; i < maxNbLoop; i++) {
                String state = (String) getServerAttribute(testName, "serverState", node);
                if ("running".equals(state)) break;
                Thread.sleep(5000);
                nbLoop++;
            }
            if (nbLoop > maxNbLoop) {
                throw new IllegalStateException("Could not ping node '" + node + "' after " + maxNbLoop + " times");
            }
        }
    }

    public static void cleanDirs(String testName, JBNode... nodes) throws Exception {
        for (JBNode node : nodes) {
            String dataDir = node.getPath() + TestUtil.PATH_SEPARATOR + "data";
            String logDir = node.getPath() + TestUtil.PATH_SEPARATOR + "log";
            String tmpDir = node.getPath() + TestUtil.PATH_SEPARATOR + "tmp";
            log.info("[" + testName + "] Cleaning dirs: " + dataDir + ", " + logDir + ", " + tmpDir);
            FileUtils.cleanDirectory(new File(dataDir));
            FileUtils.cleanDirectory(new File(logDir));
            FileUtils.cleanDirectory(new File(tmpDir));
        }
    }

    public static void prepareNodes(String testName, String queueIn, String queueOut, JBNode... nodes) throws Exception {
        // Ensure nodes are down
        executeProcess(testName, TestUtil.SCRIPT_KILL, nodes);

        // remove all files in data, log and tmp dirs
        cleanDirs(testName, nodes);

        // start nodes
        executeProcess(testName, TestUtil.SCRIPT_START, nodes);
        pingServer(testName, nodes);

        // init test with no messages in Queues: not really necessary since we cleaned the data dir...
        removeMessagesFromAllQueues(testName, queueIn, queueOut, nodes);

        // pause consuption of messages in queueIn
        invokeOP(testName, "pause", queueIn, nodes);
    }

    public static void sendMessages(String testName, CallJMSAction1 action, JBNode[] nodesIn, String jndiQueue, int nbMessages, int displayMessagesEvery)
            throws Exception {
        Connection connection = null;
        Session session = null;
        MessageProducer msgProducer = null;
        EAP6NamingContext ctx = null;
        long start = System.currentTimeMillis();
        String nodesInStr = "";
        for (JBNode jbNode : nodesIn) {
            nodesInStr += jbNode + " ";
        }
        log.info("[" + testName + "] Start sending '" + nbMessages + "' messages to queue '" + jndiQueue + "' on " + nodesInStr);
        try {
            Properties jmsLookupProps = TestUtil.createJmsProperties(nodesIn);
            ctx = new EAP6NamingContext(jmsLookupProps);
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
            Destination destinationProducer = (Destination) ctx.lookup(jndiQueue);
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            msgProducer = session.createProducer(destinationProducer);
            connection.start();

            ObjectMapper mapper = new ObjectMapper();
            action.setDisplaylogEvery(displayMessagesEvery);

            for (int i = 0; i < nbMessages; i++) {
                action.setCallId(i);
                String jSonString = mapper.writeValueAsString(action);
                TextMessage msg = session.createTextMessage(jSonString);
                msg.setStringProperty("ActionClass", action.getClass().getName());
                // msg.setIntProperty(TestUtil.JMS_HEADER_CALLID, action.getCallId());
                msg.setJMSCorrelationID("" + action.getCallId());
                msgProducer.send(msg);
                if (i % displayMessagesEvery == 0)
                    log.info("[" + testName + "] Message " + i + " sent with jmsMessageID=" + msg.getJMSMessageID() + " : " + jSonString);
            }

        } finally {
            if (msgProducer != null) msgProducer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
            if (ctx != null) ctx.close();

        }
        long end = System.currentTimeMillis();
        log.info("[" + testName + "] End sending '" + nbMessages + "' messages to queue '" + jndiQueue + "' in " + (end - start) + " ms on " + nodesInStr);

    }

    public static Object invokeOP(String testName, String operation, String queueName, JBNode... nodes) throws Exception {
        Object result = null;
        for (JBNode node : nodes) {
            long start = System.currentTimeMillis();
            JMXConnector jmxc = null;
            try {
                String queue = queueName.replaceAll("/", ".");
                ObjectName objectName = new ObjectName("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=" + queue);

                Map<String, Object> env = new HashMap<String, Object>();
                String[] credentials = new String[] { ClientJmxRolesTest.ADMINISTRATOR_USER, ClientJmxRolesTest.ADMINISTRATOR_PASSWORD };
                env.put("jmx.remote.credentials", credentials);

                JMXServiceURL url = new JMXServiceURL(node.getUrlJmx());
                jmxc = JMXConnectorFactory.connect(url, env);
                jmxc.connect();
                boolean isRegistered = jmxc.getMBeanServerConnection().isRegistered(objectName);
                if (!isRegistered) continue;
                result = jmxc.getMBeanServerConnection().invoke(objectName, operation, new Object[] {}, new String[] {});

            } finally {
                if (jmxc != null) jmxc.close();
            }

            long end = System.currentTimeMillis();
            log.info("[" + testName + "] invoke '" + operation + "' on queue '" + queueName + "' and node '" + node + "' in " + (end - start) + " ms");
        }
        return result;
    }

    public static Object getServerAttribute(String testName, String attribut, JBNode node) throws Exception {
        long start = System.currentTimeMillis();
        Object result = null;
        JMXConnector jmxc = null;
        try {
            ObjectName objectName = new ObjectName("jboss.as:management-root=server");

            Map<String, Object> env = new HashMap<String, Object>();
            String[] credentials = new String[] { ClientJmxRolesTest.ADMINISTRATOR_USER, ClientJmxRolesTest.ADMINISTRATOR_PASSWORD };
            env.put("jmx.remote.credentials", credentials);

            JMXServiceURL url = new JMXServiceURL(node.getUrlJmx());
            jmxc = JMXConnectorFactory.connect(url, env);
            jmxc.connect();

            result = jmxc.getMBeanServerConnection().getAttribute(objectName, attribut);

        } finally {
            if (jmxc != null) jmxc.close();
        }

        long end = System.currentTimeMillis();
        log.info("[" + testName + "] getAttribut '" + attribut + "'='" + result + "' in " + (end - start) + " ms");
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> browseMessages(String testName, String queueName, JBNode... nodes) throws Exception {
        List<Map<String, String>> jsonReadValue = new ArrayList<Map<String, String>>();
        StringBuilder nodesStr = new StringBuilder();
        for (JBNode node : nodes) {
            nodesStr.append(node).append(' ');
            String result = (String) invokeOP(testName, "listMessagesAsJson", queueName, node);
            ObjectMapper mapper = new ObjectMapper();
            jsonReadValue.addAll(mapper.readValue(result, List.class));
        }
        log.debug("[" + testName + "] jsonReadValue=" + jsonReadValue);
        log.info("[" + testName + "] Found '" + jsonReadValue.size() + "' messages in '" + queueName + "' on nodes: " + nodesStr);
        return jsonReadValue;
    }

    public static void removeMessagesFromAllQueues(String testName, String queueIn, String queueOut, JBNode... nodes) throws Exception {
        invokeOP(testName, "removeMessages", queueIn, nodes);
        invokeOP(testName, "removeMessages", queueIn + "Error", nodes);

        invokeOP(testName, "removeMessages", queueOut, nodes);
        invokeOP(testName, "removeMessages", queueOut + "Error", nodes);
    }

    public static Pair<Boolean, String> checkAllMessagesDelivered(String testName, List<Map<String, String>> messagesQueueOut, int nbmessageToSend) {
        List<Integer> elements = new ArrayList<Integer>(messagesQueueOut.size());
        int nbMissingElements = 0;
        int nbDuplicateElements = 0;
        StringBuilder missingStr = new StringBuilder();
        StringBuilder duplicateStr = new StringBuilder();
        for (Map<String, String> map : messagesQueueOut) {
            String callIdStr = map.get("JMSCorrelationID");
            int callId = Integer.parseInt(callIdStr);
            if (!elements.contains(callId)) elements.add(callId);
            else {
                duplicateStr.append(callIdStr).append(" (").append(map).append("),");
                nbDuplicateElements++;
            }
        }

        Collections.sort(elements);
        int i = 0;
        for (Integer intElem : elements) {
            if (intElem != i) {
                missingStr.append(i).append(',');
                nbMissingElements++;
                i++;
            }
            i++;
        }
        for (int j = i; j < nbmessageToSend; j++) {
            missingStr.append(i).append(',');
            nbMissingElements++;
        }

        String msgInfo = "[" + testName + "] nbMissingElements=" + nbMissingElements + " nbDuplicateElements=" + nbDuplicateElements;
        if (nbMissingElements > 0 || nbDuplicateElements > 0) {
            log.warn("[" + testName + "] List of missing elements: " + missingStr.toString());
            log.warn("[" + testName + "] List of duplicate elements: " + duplicateStr.toString());
            log.warn(msgInfo);
        } else {
            log.info(msgInfo);
        }
        Pair<Boolean, String> result = new ImmutablePair<Boolean, String>(nbMissingElements + nbDuplicateElements == 0, msgInfo);
        return result;
    }

    public static Pair<Boolean, String> checkAllMessagesDelivered(String testName, int nbMessagesExpected, String queueOut, int maxRetry, JBNode... nodes)
            throws Exception {
        // check all messages are in the queueOut
        Pair<Boolean, String> result = new ImmutablePair<Boolean, String>(false, "init");
        int retry = 1;
        while (!result.getKey()) {
            if (retry > maxRetry) {
                log.warn("[" + testName + "] stop browsing queues after " + (retry - 1) + " retry.");
                break;
            }
            log.info("[" + testName + "] Retry number " + retry);
            List<Map<String, String>> messagesQueueOut = JMSTestUtil.browseMessages(testName, queueOut, nodes);
            result = JMSTestUtil.checkAllMessagesDelivered(testName, messagesQueueOut, nbMessagesExpected);
            retry++;
            Thread.sleep(60000);
        }
        return result;
    }

    public static String getQeueIn(JBNode[] nodes) {
        // use only the first one because it is a cluster
        return nodes[0].getQueueIn();
    }

    public static String getQeueOut(JBNode[] nodes) {
        // use only the first one because it is a cluster
        return nodes[0].getQueueOut();
    }

    public static void runJMSTest(String testName, int nbLaunchTest, int nbMessageToSend, int displayMessagesEvery, JBNode[] nodes, JBNode[] nodesIn,
            JBNode[] nodesOut, CallJMSAction1 action, String opKillOrShutdown, JBNode nodeToKillOrShutdown) throws Exception {
        for (int i = 0; i < nbLaunchTest; i++) {
            log.info("===================> [" + testName + "] Start running test " + i);
            // Prepare Nodes: clean dirs + start nodes + clean messages in queues + pause consuption of messages in queueIn
            JMSTestUtil.prepareNodes(testName, getQeueIn(nodesIn), getQeueOut(nodesOut), nodes);

            // send messages
            JMSTestUtil.sendMessages(testName, action, nodesIn, getQeueIn(nodesIn), nbMessageToSend, displayMessagesEvery);

            // check all messages are in the queueIn
            List<Map<String, String>> messagesQueueIn = JMSTestUtil.browseMessages(testName, getQeueIn(nodesIn), nodesIn);
            if (nbMessageToSend != messagesQueueIn.size())
                throw new IllegalStateException("[" + testName + "] bad number od messages: nbMessageToSend=" + nbMessageToSend + " messagesQueueIn="
                        + messagesQueueIn.size());

            // resume the consuption of queueIn
            JMSTestUtil.invokeOP(testName, "resume", getQeueIn(nodesIn), nodesIn);

            // wait for message processing on server side
            Thread.sleep(10000);

            // Kill or clean shutdown one node then restart it
            JMSTestUtil.executeProcess(testName, opKillOrShutdown, nodeToKillOrShutdown);
            Thread.sleep(20000);
            JMSTestUtil.executeProcess(testName, TestUtil.SCRIPT_START, nodeToKillOrShutdown);

            // check all messages are in the queueOut
            Thread.sleep(60000);// wait for nodeToKillOrShutdown to be up and running + wait for recovery mecanism
            Pair<Boolean, String> result = JMSTestUtil.checkAllMessagesDelivered(testName, nbMessageToSend, getQeueOut(nodesOut), 15, nodesOut);

            // shutdown/Kill nodes to stop the test
            JMSTestUtil.executeProcess(testName, TestUtil.SCRIPT_KILL, nodes);
            if (!result.getKey())
                throw new IllegalStateException("[" + testName + "] Missing or duplicate messages after " + i + " run(s): " + result.getValue());
            log.info("===================> [" + testName + "] End running test " + i);
        }
    }

}
