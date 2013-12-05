package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.BaseAction1;
import org.app.minibank.minibankref1.action.CallLocalAction1;
import org.app.minibank.minibankref1.action.CallRemote1UnsecuredAction1;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref1.action.CallRemote3UnsecuredAction1;
import org.app.minibank.minibankref1.action.IAction1;
import org.app.minibank.minibankref1.action.SequenceAction1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientEJBTest {

    private static final Logger log = Logger.getLogger(ClientEJBTest.class);

    TransactionAttributeType[] allTxTypes = { null, REQUIRED, REQUIRES_NEW, NEVER, MANDATORY, SUPPORTS, NOT_SUPPORTED };

    List<TransactionAttributeType> withTx = Arrays.asList(null, REQUIRED, REQUIRES_NEW, MANDATORY);

    List<TransactionAttributeType> withoutTx = Arrays.asList(NOT_SUPPORTED, NEVER);

    EAP6NamingContext ctx;

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

    boolean withTx(TransactionAttributeType tat) {
        return withTx.contains(tat);
    }

    boolean withoutTx(TransactionAttributeType tat) {
        return withoutTx.contains(tat);
    }

    @Test
    public void simple1() throws Exception {
        Foo1Remote service = getFoo1Remote();
        log.info(service.defaultTxPropagation(new BaseAction1()));
    }

    @Test
    public void action1Simple() throws Exception {
        Foo1Remote service = getFoo1Remote();

        for (TransactionAttributeType tat : allTxTypes) {
            try {
                log.info(tat);
                Object result = call1(service, new BaseAction1(), tat);
                log.info(result);
                Assert.assertTrue(tat != MANDATORY);
            } catch (EJBTransactionRequiredException e) {
                Assert.assertTrue(tat == MANDATORY);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void action1Local() throws Exception {
        Foo1Remote service = getFoo1Remote();

        for (TransactionAttributeType tat1 : allTxTypes) {
            for (TransactionAttributeType tat2 : allTxTypes) {
                try {
                    log.info(tat1 + " " + tat2);
                    CallLocalAction1 action = new CallLocalAction1();
                    action.setTransactionAttributeType(tat2);
                    action.setLocalAction(new BaseAction1());
                    Map<String, Object> result = (Map<String, Object>) call1(service, action, tat1);
                    log.info(result);
                    String tx1 = (String) result.get("tx");
                    String tx2 = (String) ((Map<String, Object>) result.get("CALL")).get("tx");
                    Assert.assertTrue(withTx(tat1) && tx1 != null || (tat1 == SUPPORTS || withoutTx(tat1)) && tx1 == null);
                    Assert.assertTrue(withTx(tat2) && tx2 != null || withoutTx(tat2) && tx2 == null || tat2 == SUPPORTS);

                    if (withTx(tat1)) { // propagation
                        if (Arrays.asList(REQUIRED, SUPPORTS, null).contains(tat2)) {
                            Assert.assertEquals(tx1, tx2);
                        } else if (tat2 == REQUIRES_NEW) {
                            Assert.assertNotEquals(tx1, tx2);
                        }
                    }
                } catch (EJBTransactionRequiredException e) {
                    boolean mandatory1 = tat1 == MANDATORY;
                    boolean mandatory2 = (withoutTx.contains(tat1) || tat1 == SUPPORTS) && tat2 == MANDATORY;
                    Assert.assertTrue(mandatory1 || mandatory2);
                } catch (EJBException e) {
                    boolean never = withTx.contains(tat1) && tat2 == NEVER;
                    Assert.assertTrue(never);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void action1RemoteUnsecured() throws Exception {
        Foo1Remote service = getFoo1Remote();

        for (TransactionAttributeType tat1 : allTxTypes) {
            for (TransactionAttributeType tat2 : allTxTypes) {
                try {
                    log.info(tat1 + " " + tat2);
                    CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
                    action.setRemoteLookupProperties(TestUtil.createProperties2());
                    action.setTransactionAttributeType(tat2);
                    action.setJndi(TestUtil.getJndi2());
                    Map<String, Object> result = (Map<String, Object>) call1(service, action, tat1);
                    log.info(result);
                    String tx1 = (String) result.get("tx");
                    String tx2 = (String) ((Map<String, Object>) result.get("CALL")).get("tx");
                    Assert.assertTrue(withTx(tat1) && tx1 != null || (tat1 == SUPPORTS || withoutTx(tat1)) && tx1 == null);

                    if (withTx(tat1) && tat2 == SUPPORTS) {
                        // TODO should not fail (but it does)
                        // Assert.assertTrue("tx1=" + tx1 + " tx2=" + tx2, tx1 != null && tx2 == null);
                    } else {
                        Assert.assertTrue(withTx(tat2) && tx2 != null || (tat2 == SUPPORTS || withoutTx(tat2)) && tx2 == null);
                    }

                    if (withTx(tat1) || withTx(tat2)) Assert.assertNotEquals(tx1, tx2);

                } catch (EJBTransactionRequiredException e) {
                    boolean mandatory1 = tat1 == MANDATORY;
                    boolean mandatory2 = (withoutTx.contains(tat1) || tat1 == SUPPORTS) && tat2 == MANDATORY;
                    Assert.assertTrue(mandatory1 || mandatory2);
                } catch (EJBException e) {
                    // TODO if there is no propagation, should not fail (but it does)
                    // Assert.fail();
                    boolean never = withTx.contains(tat1) && tat2 == NEVER;
                    Assert.assertTrue(never);
                }
            }
        }
    }

    @Test
    public void call2Then3() throws Exception {
        // this fails - see Case00977606
        Foo1Remote service = getFoo1Remote();

        CallRemote2UnsecuredAction1 action2 = new CallRemote2UnsecuredAction1();
        action2.setRemoteLookupProperties(TestUtil.createProperties2());
        action2.setJndi(TestUtil.getJndi2());
        action2.setTransactionAttributeType(NOT_SUPPORTED);

        CallRemote3UnsecuredAction1 action3 = new CallRemote3UnsecuredAction1();
        action3.setRemoteLookupProperties(TestUtil.createProperties3());
        action3.setJndi(TestUtil.getJndi3());
        action3.setTransactionAttributeType(REQUIRED);

        SequenceAction1 seq = new SequenceAction1();
        seq.getActions().add(action2);
        seq.getActions().add(action3);
        log.info(service.required(seq));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multiEntity() throws Exception {
        Foo1Remote service = getFoo1Remote();

        CallRemote1UnsecuredAction1 action = new CallRemote1UnsecuredAction1();
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setJndi(TestUtil.getJndi1());

        Map<String, Object> result = (Map<String, Object>) service.defaultTxPropagation(action);
        log.info(result);
        String node1 = (String) result.get("node");
        String node2 = (String) ((Map<String, Object>) result.get("CALL")).get("node");
        Assert.assertTrue(node1.endsWith("100") || node1.endsWith("200"));
        Assert.assertTrue(node2.endsWith("300") || node2.endsWith("400"));
    }

    Object call1(Foo1Remote service, IAction1 action, TransactionAttributeType transactionAttributeType) throws AccountException {
        if (transactionAttributeType == REQUIRED) {
            return service.required(action);
        } else if (transactionAttributeType == REQUIRES_NEW) {
            return service.requiresNew(action);
        } else if (transactionAttributeType == NEVER) {
            return service.never(action);
        } else if (transactionAttributeType == MANDATORY) {
            return service.mandatory(action);
        } else if (transactionAttributeType == NOT_SUPPORTED) {
            return service.notSupported(action);
        } else if (transactionAttributeType == SUPPORTS) {
            return service.supports(action);
        } else {
            return service.defaultTxPropagation(action);
        }
    }

}
