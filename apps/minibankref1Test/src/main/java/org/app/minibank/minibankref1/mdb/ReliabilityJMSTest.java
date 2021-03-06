package org.app.minibank.minibankref1.mdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.app.minibank.minibankref.JBNode;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMSAction1;
import org.app.minibank.minibankref1.action.CallJPAAction1;
import org.junit.Ignore;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters()
public class ReliabilityJMSTest {

    @Parameter(names = { "-tests", "-t" }, description = "coma-separated list of tests to execute (could be empty or 1 2 3 4 ...) default (empty) will execute all tests", required = false)
    private List<Integer> tests = new ArrayList<Integer>();

    @Parameter(names = { "-loop", "-l" }, description = "loop execution time (default to 3)", required = false)
    int loop = 5;

    @Parameter(names = { "-nbmsg", "-n" }, description = "nb JMS messages to send (default to 10000)", required = false)
    int nbmsg = 10000;

    private DbType dbType = DbType.H2;

    @Test
    public void localLocalShutdownTest1() throws Exception {
        String tn = "Local-Local-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void localLocalKillTest2() throws Exception {
        String tn = "Local-Local-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void localRemoteShutdownTest3() throws Exception {
        String tn = "Local-Remote-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, nodesOut);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void localRemoteKillTest4() throws Exception {
        String tn = "Local-Remote-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, nodesOut);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteLocalShutdownTest5() throws Exception {
        String tn = "Remote-Local-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodes = ArrayUtils.addAll(nodesOut, nodesIn);
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteLocalKillTest6() throws Exception {
        String tn = "Remote-Local-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodes = ArrayUtils.addAll(nodesOut, nodesIn);
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteRemoteShutdownTest7() throws Exception {
        String tn = "Remote-Remote-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteRemoteKillTest8() throws Exception {
        String tn = "Remote-Remote-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void localDatabaseShutdownTest9() throws Exception {
        String tn = "Local-Database-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJPAAction1 action = new CallJPAAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void localDatabaseKillTest10() throws Exception {
        String tn = "Local-Database-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJPAAction1 action = new CallJPAAction1();
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = nodesIn;
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteDatabaseShutdownTest11() throws Exception {
        String tn = "remote-Database-Shutdown";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJPAAction1 action = new CallJPAAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        String opKillOrShutdown = TestUtil.SCRIPT_SHUTDOWN;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    public void remoteDatabaseKillTest12() throws Exception {
        String tn = "remote-Database-Kill";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJPAAction1 action = new CallJPAAction1();
        JBNode[] nodesIn = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodesOut = nodesIn;
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, TestUtil.node1A, TestUtil.node1B);
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.runJMSTest();
    }

    @Test
    @Ignore
    public void simpleTest13() throws Exception {
        String tn = "Simple";
        int nbLaunchTest = loop;
        int nbMsgToSend = nbmsg;
        int displayMsgEvery = 500;
        DbType db = dbType;
        CallJMSAction1 action = new CallJMSAction1();
        action.setSendToLocalQueue(false);
        JBNode[] nodesIn = { TestUtil.node1A, TestUtil.node1B };
        JBNode[] nodesOut = { TestUtil.node2A, TestUtil.node2B };
        JBNode[] nodes = ArrayUtils.addAll(nodesIn, nodesOut);
        action.setRemoteLookupProperties(TestUtil.createJmsProperties(nodesOut));
        String opKillOrShutdown = TestUtil.SCRIPT_KILL;
        JBNode nodeForOp = TestUtil.node1A;

        JMSTestUtil test = new JMSTestUtil(tn, nbLaunchTest, nbMsgToSend, displayMsgEvery, db, nodes, nodesIn, nodesOut, action, opKillOrShutdown, nodeForOp);
        test.checkAllMessagesDelivered(test.getQeueOut(), 15, 1, nodesOut);
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
            rjt.tests.add(9);
            rjt.tests.add(10);
            rjt.tests.add(11);
            rjt.tests.add(12);
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
                rjt.localDatabaseShutdownTest9();
                break;
            case 10:
                rjt.localDatabaseKillTest10();
                break;
            case 11:
                rjt.remoteDatabaseShutdownTest11();
                break;
            case 12:
                rjt.remoteDatabaseKillTest12();
                break;
            case 13:
                rjt.simpleTest13();
                break;

            default:
                break;
            }
        }
    }
}
