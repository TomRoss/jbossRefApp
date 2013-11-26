package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Map;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Case00024904 {

    private static final Logger log = Logger.getLogger(Case00024904.class);

    EAP6NamingContext ctx1;

    Foo1Remote service1;

    CallRemote2UnsecuredAction1 action;

    @Before
    public void before() throws Exception {
        ctx1 = new EAP6NamingContext(TestUtil.createProperties1());
        service1 = (Foo1Remote) ctx1.lookup(TestUtil.getJndi1());
        action = new CallRemote2UnsecuredAction1();
        action.setJndi(TestUtil.getJndi2());
        action.setRemoteLookupProperties(TestUtil.createProperties2());
    }

    @After
    public void after() throws Exception {
        if (ctx1 != null) ctx1.close();
    }

    void callServer2WithPauseAsynch() {
        new Thread() {
            @Override
            public void run() {
                try {
                    EAP6NamingContext ctx2 = new EAP6NamingContext(TestUtil.createProperties2());
                    Foo2Remote service2 = (Foo2Remote) ctx2.lookup(TestUtil.getJndi2());
                    service2.defaultTxPropagation(new BaseAction2().setPauseBefore(60000));
                    ctx2.close();
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
            }
        }.start();
    }

    // ------------ COMPLETED_NO

    @SuppressWarnings("unchecked")
    @Test
    public void useCase2_REQUIRED_REQUIRED_COMPLETED_NO() throws Exception {
        // asynch calls fails with Exception in thread "Thread-3" java.lang.AssertionError: java.io.IOException: Channel Channel ID af9dc7a5 (outbound) of
        // Remoting connection 361cb7a1 to DEVPC016970/10.23.132.245:5401 has been closed
        callServer2WithPauseAsynch();
        log.info("please shutdown gracefully server from cluster 2 where the call has landed");
        Thread.sleep(10000);
        action.setTransactionAttributeType(REQUIRED);
        Object last = null;
        for (int i = 0; i < 10; i++) {
            log.info(last = service1.required(action));
        }
        int globalCount = (Integer) ((Map<String, Object>) ((Map<String, Object>) last).get("CALL")).get("globalCount");
        Assert.assertEquals(10, globalCount);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void useCase4_REQUIRED_NOTSUPPORTED_COMPLETED_NO() throws Exception {
        // asynch calls fails with Exception in thread "Thread-3" java.lang.AssertionError: java.io.IOException: Channel Channel ID edee0534 (outbound) of
        // Remoting connection 7f26d3df to null has been closed
        callServer2WithPauseAsynch();
        log.info("please shutdown gracefully server from cluster 2 where the call has landed");
        Thread.sleep(10000);
        action.setTransactionAttributeType(NOT_SUPPORTED);
        Object last = null;
        for (int i = 0; i < 10; i++) {
            log.info(last = service1.required(action));
        }
        int globalCount = (Integer) ((Map<String, Object>) ((Map<String, Object>) last).get("CALL")).get("globalCount");
        Assert.assertEquals(10, globalCount);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void useCase10_NOTSUPPORTED_REQUIRED_COMPLETED_NO() throws Exception {
        // Exception in thread "Thread-3" java.lang.AssertionError: java.io.IOException: Channel Channel ID d4ab8ae3 (outbound) of Remoting connection 655d616e
        // to DEVPC016970/10.23.132.245:5301 has been closed
        callServer2WithPauseAsynch();
        log.info("please shutdown gracefully server from cluster 2 where the call has landed");
        Thread.sleep(10000);
        action.setTransactionAttributeType(REQUIRED);
        Object last = null;
        for (int i = 0; i < 10; i++) {
            log.info(last = service1.notSupported(action));
        }
        int globalCount = (Integer) ((Map<String, Object>) ((Map<String, Object>) last).get("CALL")).get("globalCount");
        Assert.assertEquals(10, globalCount);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void useCase12_NOTSUPPORTED_NOTSUPPORTED_COMPLETED_NO() throws Exception {
        // Exception in thread "Thread-3" java.lang.AssertionError: java.io.IOException: Channel Channel ID fbd5c416 (outbound) of Remoting connection 2e257f1b
        // to DEVPC016970/10.23.132.245:5301 has been closed
        callServer2WithPauseAsynch();
        log.info("please shutdown gracefully server from cluster 2 where the call has landed");
        Thread.sleep(10000);
        action.setTransactionAttributeType(NOT_SUPPORTED);
        Object last = null;
        for (int i = 0; i < 10; i++) {
            log.info(last = service1.notSupported(action));
        }
        int globalCount = (Integer) ((Map<String, Object>) ((Map<String, Object>) last).get("CALL")).get("globalCount");
        Assert.assertEquals(10, globalCount);
    }

    // ----------- MAY BE

    @Test
    public void useCase6_REQUIRED_REQUIRED_COMPLETED_MAYBE() throws Exception {
        // test OK in 6.2
        try {
            log.info("please kill server from cluster 2 where the call has landed");
            action.setAction2(new BaseAction2().setPauseBefore(20000L));
            action.setTransactionAttributeType(REQUIRED);
            log.info(service1.required(action));
            Assert.fail();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void useCase8_REQUIRED_NOTSUPPORTED_COMPLETED_MAYBE() throws Exception {
        // test OK in 6.2
        try {
            log.info("please kill server from cluster 2 where the call has landed");
            action.setAction2(new BaseAction2().setPauseBefore(20000L));
            action.setTransactionAttributeType(NOT_SUPPORTED);
            log.info(service1.required(action));
            Assert.fail();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void useCase14_NOTSUPPORTED_REQUIRED_COMPLETED_MAYBE() throws Exception {
        // test OK in 6.2
        try {
            log.info("please kill server from cluster 2 where the call has landed");
            action.setAction2(new BaseAction2().setPauseBefore(20000L));
            action.setTransactionAttributeType(REQUIRED);
            log.info(service1.notSupported(action));
            Assert.fail();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void useCase16_NOTSUPPORTED_NOTSUPPORTED_COMPLETED_MAYBE() throws Exception {
        // test OK in 6.2
        try {
            log.info("please kill server from cluster 2 where the call has landed");
            action.setAction2(new BaseAction2().setPauseBefore(20000L));
            action.setTransactionAttributeType(NOT_SUPPORTED);
            log.info(service1.notSupported(action));
            Assert.fail();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
