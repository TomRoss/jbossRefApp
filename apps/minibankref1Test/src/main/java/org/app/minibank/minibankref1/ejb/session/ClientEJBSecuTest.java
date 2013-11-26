package org.app.minibank.minibankref1.ejb.session;

import java.util.Map;

import javax.ejb.EJBAccessException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.BaseAction1;
import org.app.minibank.minibankref1.action.CallLocalAction1;
import org.app.minibank.minibankref1.action.CallRemote2SecuredAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1SecureRemote;
import org.junit.Assert;
import org.junit.Test;

public class ClientEJBSecuTest {

    private static final Logger log = Logger.getLogger(ClientEJBSecuTest.class);

    @SuppressWarnings("unchecked")
    @Test
    public void doSecureActionCluster1Only() throws Exception {
        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1(TestUtil.userapp1, TestUtil.passapp1));
        Foo1SecureRemote service = (Foo1SecureRemote) ctx1.lookup(TestUtil.getSecureJndi1());
        Map<String, Object> result = (Map<String, Object>) service.doSecureAction(new BaseAction1());
        log.info(result);
        Assert.assertTrue(result.get("user").equals(TestUtil.userapp1));
        ctx1.close();
    }

    @Test
    public void doSecureActionRejectedCluster1Only() throws Exception {
        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1(TestUtil.userapp3, TestUtil.passapp3));
        Foo1SecureRemote service = (Foo1SecureRemote) ctx1.lookup(TestUtil.getSecureJndi1());

        try {
            service.doSecureAction(new BaseAction1());
            Assert.fail();
        } catch (EJBAccessException e) {
            // Expected Exception
        }

        ctx1.close();
    }

    @Test
    public void doSecureActionCluster12() throws Exception {
        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1(TestUtil.userapp1, TestUtil.passapp1));
        Foo1SecureRemote service = (Foo1SecureRemote) ctx1.lookup(TestUtil.getSecureJndi1());

        CallRemote2SecuredAction1 action = new CallRemote2SecuredAction1();
        action.setJndi(TestUtil.getSecureJndi2());
        action.setRemoteLookupProperties(TestUtil.createProperties2(null, null));

        // Don't want to do this because I want to propagate security context
        // GenericConnection connection2 = new GenericConnection(jndi2, TestUtil.createProperties2(TestUtil.userapp1, TestUtil.passapp1), true);

        String result = (String) service.doSecureAction(action);
        log.info(result);

        ctx1.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void doSecureActionCluster1OnlyWith2users() throws Exception {

        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1(TestUtil.userapp1, TestUtil.passapp1));
        Foo1SecureRemote service1 = (Foo1SecureRemote) ctx1.lookup(TestUtil.getSecureJndi1());
        Map<String, Object> result1 = (Map<String, Object>) service1.doSecureAction(new CallLocalAction1());
        log.info(result1);
        Assert.assertTrue(result1.get("user").equals(TestUtil.userapp1));

        EAP6NamingContext ctx2 = new EAP6NamingContext(TestUtil.createProperties1(TestUtil.userapp2, TestUtil.passapp2));
        Foo1SecureRemote service2 = (Foo1SecureRemote) ctx2.lookup(TestUtil.getSecureJndi1());
        Map<String, Object> result2 = (Map<String, Object>) service2.doSecureAction(new CallLocalAction1());
        log.info(result2);
        Assert.assertTrue(result2.get("user").equals(TestUtil.userapp2));

        result1 = (Map<String, Object>) service1.doSecureAction(new CallLocalAction1());
        log.info(result1);
        Assert.assertTrue(result1.get("user").equals(TestUtil.userapp1));

        result2 = (Map<String, Object>) service2.doSecureAction(new CallLocalAction1());
        log.info(result2);
        Assert.assertTrue(result2.get("user").equals(TestUtil.userapp2));

        ctx1.close();
        ctx2.close();
    }

}
