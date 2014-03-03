package org.app.minibank.minibankref1.mdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.JBNode;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMSAction1;
import org.app.minibank.minibankref1.jmx.ClientJmxRolesTest;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JMSTestUtil {

    private static final Logger log = Logger.getLogger(JMSTestUtil.class);

    public static final String CR = System.getProperty("line.separator");

    private final String testName;

    private final int nbLaunchTest;

    private final int nbMessageToSend;

    private final int displayMsgEvery;

    private final JBNode[] nodes;

    private final JBNode[] nodesIn;

    private final JBNode[] nodesOut;

    private final CallJMSAction1 action;

    private final String opKillOrShutdown;

    private final JBNode nodeToKillOrShutdown;

    private int currentLoop = 1;

    public JMSTestUtil(String testName, int nbLaunchTest, int nbMessageToSend, int displayMsgEvery, JBNode[] nodes, JBNode[] nodesIn, JBNode[] nodesOut,
            CallJMSAction1 action, String opKillOrShutdown, JBNode nodeToKillOrShutdown) {
        this.testName = testName;
        this.nbLaunchTest = nbLaunchTest;
        this.nbMessageToSend = nbMessageToSend;
        this.displayMsgEvery = displayMsgEvery;
        this.nodes = nodes;
        this.nodesIn = nodesIn;
        this.nodesOut = nodesOut;
        this.action = action;
        this.opKillOrShutdown = opKillOrShutdown;
        this.nodeToKillOrShutdown = nodeToKillOrShutdown;

    }

    public void executeProcess(String commandToExec, JBNode... nodesToExecCmd) throws Exception {
        for (JBNode node : nodesToExecCmd) {
            String nodeScript = node.getPath() + TestUtil.PATH_SEPARATOR + commandToExec;
            File fileNode = new File(nodeScript);
            String line = "cmd /C " + fileNode.getName();
            log.info(getMesageInfo() + " start launching '" + line + "'");
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(fileNode.getParentFile());
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor(5000);
            log.info(getMesageInfo() + " end launching '" + line + "'");
        }
    }

    public void pingServer(JBNode... nodesToPing) throws Exception {
        for (JBNode node : nodesToPing) {
            int nbLoop = 0;
            int maxNbLoop = 10;
            for (int i = 0; i < maxNbLoop; i++) {
                String state = (String) getServerAttribute("serverState", node);
                if ("running".equals(state)) break;
                Thread.sleep(5000);
                nbLoop++;
            }
            if (nbLoop > maxNbLoop) {
                throw new IllegalStateException("Could not ping node '" + node + "' after " + maxNbLoop + " times");
            }
        }
    }

    public void cleanDirs(JBNode... nodesToClean) throws Exception {
        for (JBNode node : nodesToClean) {
            String dataDir = node.getPath() + TestUtil.PATH_SEPARATOR + "data";
            String logDir = node.getPath() + TestUtil.PATH_SEPARATOR + "log";
            String tmpDir = node.getPath() + TestUtil.PATH_SEPARATOR + "tmp";
            log.info(getMesageInfo() + " Cleaning dirs: " + dataDir + ", " + logDir + ", " + tmpDir);
            FileUtils.cleanDirectory(new File(dataDir));
            FileUtils.cleanDirectory(new File(logDir));
            FileUtils.cleanDirectory(new File(tmpDir));
        }
    }

    public void prepareNodes() throws Exception {
        // Ensure nodes are down
        executeProcess(TestUtil.SCRIPT_KILL, nodes);

        // remove all files in data, log and tmp dirs
        cleanDirs(nodes);

        // start nodes
        executeProcess(TestUtil.SCRIPT_START, nodes);
        pingServer(nodes);

        // init test with no messages in Queues: not really necessary since we cleaned the data dir...
        removeMessagesFromAllQueues();

        // pause consuption of messages in queueIn
        invokeOP("pause", getQeueIn(), nodes);
    }

    public void sendMessages() throws Exception {
        Connection connection = null;
        Session session = null;
        MessageProducer msgProducer = null;
        EAP6NamingContext ctx = null;
        long start = System.currentTimeMillis();
        String nodesInStr = "";
        for (JBNode jbNode : nodesIn) {
            nodesInStr += jbNode + " ";
        }
        log.info(getMesageInfo() + " Start sending '" + nbMessageToSend + "' messages to queue '" + getQeueIn() + "' on " + nodesInStr);
        try {
            Properties jmsLookupProps = TestUtil.createJmsProperties(nodesIn);
            ctx = new EAP6NamingContext(jmsLookupProps);
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
            Destination destinationProducer = (Destination) ctx.lookup(getQeueIn());
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            msgProducer = session.createProducer(destinationProducer);
            connection.start();

            ObjectMapper mapper = new ObjectMapper();
            action.setDisplaylogEvery(displayMsgEvery);

            for (int i = 0; i < nbMessageToSend; i++) {
                action.setCallId(i);
                String jSonString = mapper.writeValueAsString(action);
                TextMessage msg = session.createTextMessage(jSonString);
                msg.setStringProperty("ActionClass", action.getClass().getName());
                // msg.setIntProperty(TestUtil.JMS_HEADER_CALLID, action.getCallId());
                msg.setJMSCorrelationID("" + action.getCallId());
                msgProducer.send(msg);
                if (i % displayMsgEvery == 0)
                    log.info(getMesageInfo() + " Message " + i + " sent with jmsMessageID=" + msg.getJMSMessageID() + " : " + jSonString);
            }

        } finally {
            if (msgProducer != null) msgProducer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
            if (ctx != null) ctx.close();

        }
        long end = System.currentTimeMillis();
        log.info(getMesageInfo() + " End sending '" + nbMessageToSend + "' messages to queue '" + getQeueIn() + "' in " + (end - start) + " ms on "
                + nodesInStr);

    }

    public Object invokeOP(String operation, String queueName, JBNode... nodesToInvokeOP) throws Exception {
        Object result = null;
        for (JBNode node : nodesToInvokeOP) {
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
            log.info(getMesageInfo() + " invoke '" + operation + "' on queue '" + queueName + "' and node '" + node + "' in " + (end - start) + " ms");
        }
        return result;
    }

    public Object getServerAttribute(String attribut, JBNode node) throws Exception {
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
        log.info(getMesageInfo() + " getAttribut '" + attribut + "'='" + result + "' in " + (end - start) + " ms");
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> browseMessages(String queueNameToInspect, JBNode... nodesToBrowse) throws Exception {
        List<Map<String, String>> jsonReadValue = new ArrayList<Map<String, String>>();
        StringBuilder nodesStr = new StringBuilder();
        for (JBNode node : nodesToBrowse) {
            nodesStr.append(node).append(' ');
            String result = (String) invokeOP("listMessagesAsJson", queueNameToInspect, node);
            ObjectMapper mapper = new ObjectMapper();
            jsonReadValue.addAll(mapper.readValue(result, List.class));
        }
        log.debug(getMesageInfo() + " jsonReadValue=" + jsonReadValue);
        log.info(getMesageInfo() + " Found '" + jsonReadValue.size() + "' messages in '" + queueNameToInspect + "' on nodes: " + nodesStr);
        return jsonReadValue;
    }

    public void removeMessagesFromAllQueues() throws Exception {
        invokeOP("removeMessages", getQeueIn(), nodes);
        invokeOP("removeMessages", getQeueIn() + "Error", nodes);

        invokeOP("removeMessages", getQeueOut(), nodes);
        invokeOP("removeMessages", getQeueOut() + "Error", nodes);
    }

    public JMSResult checkMessagesDelivered(List<Map<String, String>> messagesQueueOut, int loopNumber, int retryNumber) {
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
        for (int j = i; j < nbMessageToSend; j++) {
            missingStr.append(j).append(',');
            nbMissingElements++;
        }

        String infoNumbers = "nbMissingElements=" + nbMissingElements + " nbDuplicateElements=" + nbDuplicateElements;
        String msgInfo = getMesageInfo() + " " + infoNumbers;
        if (nbMissingElements > 0 || nbDuplicateElements > 0) {
            log.warn(getMesageInfo() + " List of missing elements: " + missingStr.toString());
            log.warn(getMesageInfo() + " List of duplicate elements: " + duplicateStr.toString());
            log.warn(msgInfo);
        } else {
            log.info(msgInfo);
        }
        JMSResult result = new JMSResult(testName, loopNumber, retryNumber, (nbMissingElements + nbDuplicateElements == 0), infoNumbers);
        return result;
    }

    public JMSAllResults checkAllMessagesDelivered(String queueNameToInspect, int maxRetry, int loopNumber, JBNode... nodesToBrowse) throws Exception {
        // check all messages are in the queueOut
        JMSAllResults results = new JMSAllResults(testName, currentLoop);
        int retry = 1;
        while (!results.isAllDelivered()) {
            if (retry > maxRetry) {
                log.warn(getMesageInfo() + " stop browsing queues after " + (retry - 1) + " retry.");
                break;
            }
            log.info(getMesageInfo() + " Retry number " + retry);
            List<Map<String, String>> messagesQueueOut = browseMessages(queueNameToInspect, nodesToBrowse);
            JMSResult resTempo = checkMessagesDelivered(messagesQueueOut, loopNumber, retry);
            results.add(resTempo);
            retry++;
            if (!resTempo.isAllDelivered()) Thread.sleep(60000);
        }
        return results;
    }

    public String getQeueIn() {
        // use only the first one because it is a cluster
        return nodesIn[0].getQueueIn();
    }

    public String getQeueOut() {
        // use only the first one because it is a cluster
        return nodesOut[0].getQueueOut();
    }

    private String getMesageInfo() {
        String info = "[" + testName + " (loop=" + currentLoop + ")]";
        return info;
    }

    public void runJMSTest() throws Exception {
        log.info("******************** Start running " + testName + " test");
        long startTime = System.currentTimeMillis();
        LinkedList<JMSAllResults> allResults = new LinkedList<JMSAllResults>();
        for (currentLoop = 1; currentLoop <= nbLaunchTest; currentLoop++) {
            long startTimeLoop = System.currentTimeMillis();
            String info = getMesageInfo();
            log.info("===================> " + info + " Start running test " + currentLoop);
            // Prepare Nodes: clean dirs + start nodes + clean messages in queues + pause consuption of messages in queueIn
            prepareNodes();

            // send messages
            sendMessages();

            // check all messages are in the queueIn
            List<Map<String, String>> messagesQueueIn = browseMessages(getQeueIn(), nodesIn);
            if (nbMessageToSend != messagesQueueIn.size())
                throw new IllegalStateException(info + " bad number od messages: nbMessageToSend=" + nbMessageToSend + " messagesQueueIn="
                        + messagesQueueIn.size());

            // resume the consuption of queueIn
            invokeOP("resume", getQeueIn(), nodesIn);

            // wait for message processing on server side
            Thread.sleep(10000);

            // Kill or clean shutdown one node then restart it
            executeProcess(opKillOrShutdown, nodeToKillOrShutdown);
            Thread.sleep(20000);
            executeProcess(TestUtil.SCRIPT_START, nodeToKillOrShutdown);

            // check all messages are in the queueOut
            Thread.sleep(60000);// wait for nodeToKillOrShutdown to be up and running + wait for recovery mecanism
            JMSAllResults resultsTempo = checkAllMessagesDelivered(getQeueOut(), 15, currentLoop, nodesOut);
            long endTimeLoop = System.currentTimeMillis();
            resultsTempo.setTimeInMs(endTimeLoop - startTimeLoop);
            log.info(resultsTempo);
            allResults.addLast(resultsTempo);

            // shutdown/Kill nodes to stop the test
            executeProcess(TestUtil.SCRIPT_KILL, nodes);

            if (!resultsTempo.isAllDelivered())
                throw new IllegalStateException(info + " Missing or duplicate messages after " + currentLoop + " run(s): " + resultsTempo.toString());
            log.info("===================> " + info + " End running test " + currentLoop);
        }
        log.info("******************** End running " + testName + " test");

        long endTime = System.currentTimeMillis();
        displayResults(allResults, endTime - startTime);
    }

    private void displayResults(LinkedList<JMSAllResults> allResults, long totalTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("All results for test '").append(testName).append("' executed in ").append(totalTime).append(" ms").append(CR);
        for (JMSAllResults jmsAllResults : allResults) {
            sb.append(jmsAllResults.toString());
        }
        log.info(sb.toString());
    }

}
