package org.app.minibank.minibankref1.mdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.app.minibank.minibankref.JBNode;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMSAction1;
import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters()
public class ReliabilityJMSTest {

    @Parameter(names = { "-tests", "-t" }, description = "space-separated list of tests to execute (could be empty or 1 2 3 4 ...) default (empty) will execute all tests", required = false)
    private List<Integer> tests = new ArrayList<Integer>();

    @Parameter(names = { "-loop", "-l" }, description = "loop execution time (default to 3)", required = false)
    int loop = 3;

    @Parameter(names = { "-nbmsg", "-n" }, description = "nb JMS messages to send (default to 10000)", required = false)
    int nbmsg = 10000;

    private static final Logger log = Logger.getLogger(ReliabilityJMSTest.class);

    @Test
    public void localLocalShutdownTest1() throws Exception {
        String tn = "Local-Local-Shutdown";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void localLocalKillTest2() throws Exception {
        String tn = "Local-Local-Kill";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void localRemoteShutdownTest3() throws Exception {
        String tn = "Local-Remote-Shutdown";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, nodesOut);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void localRemoteKillTest4() throws Exception {
        String tn = "Local-Remote-Kill";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, nodesOut);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void remoteLocalShutdownTest5() throws Exception {
        String tn = "Remote-Local-Shutdown";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodes = ArrayUtils.addAll(nodesOut, nodesIn);
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void remoteLocalKillTest6() throws Exception {
        String tn = "Remote-Local-Kill";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodes = ArrayUtils.addAll(nodesOut, nodesIn);
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void remoteRemoteShutdownTest7() throws Exception {
        String tn = "Remote-Remote-Shutdown";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    public void remoteRemoteKillTest8() throws Exception {
        String tn = "Remote-Remote-Kill";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        int nbLaunchTest = loop;
        int nbMessageToSend = nbmsg;
        int displayMsgEvery = 500;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        JBNode nodeToKillOrShutdown = TestUtil.node1A;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;

        JMSTestUtil.runJMSTest(tn, nbLaunchTest, nbMessageToSend, displayMsgEvery, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeToKillOrShutdown);
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    @Test
    // @Ignore
    public void simpleTest9() throws Exception {
        String tn = "Simple";
        log.info("**************** Start running " + tn + " test");
        long start = System.currentTimeMillis();
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        // CallJMSAction1 action = new CallJMSAction1();
        // JMSTestUtil.prepareNodes(tn, JMSTestUtil.getQeueIn(nodesOut), JMSTestUtil.getQeueOut(nodesOut), nodesOut);
        // JMSTestUtil.sendMessages(tn, action, nodesOut, JMSTestUtil.getQeueIn(nodesOut), nbmsg, 1);
        // JMSTestUtil.invokeOP(tn, "resume", JMSTestUtil.getQeueIn(nodesOut), nodesOut);
        Pair<Boolean, String> result = JMSTestUtil.checkAllMessagesDelivered(tn, nbmsg, JMSTestUtil.getQeueOut(nodesOut), 10, nodesOut);
        Assert.assertTrue(result.getValue(), result.getKey());
        long stop = System.currentTimeMillis();
        log.info("**************** End running " + tn + " test in " + (stop - start) + " ms.");
    }

    public static void main(String[] args) throws Exception {
        ReliabilityJMSTest rjt = new ReliabilityJMSTest();
        new JCommander(rjt, args);
        if (rjt.tests.size() == 0) {
            rjt.tests.add(1);
            rjt.tests.add(2);
            rjt.tests.add(3);
            rjt.tests.add(4);
            rjt.tests.add(5);
            rjt.tests.add(6);
            rjt.tests.add(7);
            rjt.tests.add(8);
        }
        for (int test : rjt.tests) {
            switch (test) {
            case 1:
                rjt.localLocalShutdownTest1();
                break;
            case 2:
                rjt.localLocalKillTest2();
                break;
            case 3:
                rjt.localRemoteShutdownTest3();
                break;
            case 4:
                rjt.localRemoteKillTest4();
                break;
            case 5:
                rjt.remoteLocalShutdownTest5();
                break;
            case 6:
                rjt.remoteLocalKillTest6();
                break;
            case 7:
                rjt.remoteRemoteShutdownTest7();
                break;
            case 8:
                rjt.remoteRemoteKillTest8();
                break;
            case 9:
                rjt.simpleTest9();
                break;

            default:
                break;
            }
        }
    }
}
