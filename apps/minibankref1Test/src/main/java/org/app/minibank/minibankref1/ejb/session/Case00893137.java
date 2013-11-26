package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.BaseAction1;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Case00893137 {

    private static final Logger log = Logger.getLogger(Case00893137.class);

    EAP6NamingContext ctx;

    @Test
    public void callForward1() throws Exception {
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setTransactionAttributeType(REQUIRED);
        action.setJndi(TestUtil.getJndi2());
        Object result = getFoo1Remote().required(action);
        log.info(result);
    }

    @Test
    public void callForward2() throws Exception {
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setTransactionAttributeType(REQUIRED);
        action.setJndi("ejb:" + TestUtil.getJndi2());
        Object result = getFoo1Remote().required(action);
        log.info(result);
    }

    @Test
    public void callForward() throws Exception {

        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setTransactionAttributeType(REQUIRED);
        action.setJndi(TestUtil.getJndi2());
        action.setRepeat(10);

        for (int i = 0; i < 100; i++) {
            log.info(getFoo1Remote().required(action));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void requiredThenSupports() throws Exception {
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setTransactionAttributeType(SUPPORTS);
        action.setJndi(TestUtil.getJndi2());

        Map<String, Object> result = (Map<String, Object>) getFoo1Remote().required(action);
        log.info(result);

        String tx1 = (String) result.get("tx");
        String tx2 = (String) ((Map<String, Object>) result.get("CALL")).get("tx");

        Assert.assertNotNull(tx1);
        Assert.assertNotEquals(tx1, tx2);
        Assert.assertNull("tx2", tx2);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void requiredThenNever() throws Exception {
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setTransactionAttributeType(NEVER);
        action.setJndi(TestUtil.getJndi2());

        Map<String, Object> result = (Map<String, Object>) getFoo1Remote().required(action);
        log.info(result);
    }

    @Test
    public void lagTimeWithNode1BDown() throws Exception {
        // start servers 1A, 2A, 2B
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            EAP6NamingContext myctx = new EAP6NamingContext(TestUtil.createProperties1());
            Foo1Remote service = (Foo1Remote) ctx.lookup(TestUtil.getJndi1());
            Object result = service.required(new BaseAction1());
            long duration = System.currentTimeMillis() - start;
            log.info(duration + " ms; " + result);
            myctx.close();
        }
    }

    @Before
    public void before() throws NamingException {
        ctx = new EAP6NamingContext(TestUtil.createProperties1());
    }

    @After
    public void after() throws NamingException {
        if (ctx != null) ctx.close();
    }

    private Foo1Remote getFoo1Remote() throws NamingException {
        return (Foo1Remote) ctx.lookup(TestUtil.getJndi1());
    }

}
