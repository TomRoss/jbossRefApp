package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Map;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;
import org.junit.Assert;
import org.junit.Test;

public class Case00023643 {

    private static final Logger log = Logger.getLogger(Case00023643.class);

    @SuppressWarnings("unchecked")
    @Test
    public void callForwardScenarioJune14() throws Exception {

        log.info("calling cluster 2 with sleep = 60 secs");

        new Thread() {
            @Override
            public void run() {
                try {
                    EAP6NamingContext ctx2 = new EAP6NamingContext(TestUtil.createProperties2());
                    Foo2Remote service2 = (Foo2Remote) ctx2.lookup(TestUtil.getJndi2());
                    BaseAction2 action = new BaseAction2();
                    action.setPauseBefore(60000);
                    service2.defaultTxPropagation(action);
                    ctx2.close();
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
            }
        }.start();

        log.info("waiting 20 secs before making calls to cluster 1 => please shutdown server from cluster 2 that received the long running call");
        Thread.sleep(20000);
        log.info("starting calling cluster 1, which will forward the call to cluster 2");

        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1());
        Foo1Remote service1 = (Foo1Remote) ctx1.lookup(TestUtil.getJndi1());
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setJndi(TestUtil.getJndi2());
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        Object last = null;

        for (int i = 0; i < 1000; i++) {
            last = service1.required(action);
            log.info(last);
        }

        ctx1.close();
        Map<String, Object> callResult = (Map<String, Object>) ((Map<String, Object>) last).get("CALL");
        Assert.assertEquals(999, callResult.get("globalCount"));
    }

    @Test
    public void callForwardInitialCase() throws Exception {
        EAP6NamingContext ctx1 = new EAP6NamingContext(TestUtil.createProperties1());
        Foo1Remote service1 = (Foo1Remote) ctx1.lookup(TestUtil.getJndi1());

        log.info("please shutdown gracefully server on cluster 2 where the call has landed in a few seconds");
        CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
        action.setJndi(TestUtil.getJndi2());
        action.setRemoteLookupProperties(TestUtil.createProperties2());
        action.setAction2(new BaseAction2().setPauseAfter(20000));
        action.setTransactionAttributeType(REQUIRED);
        action.setPauseAfter(20000);

        log.info(service1.required(action));
        ctx1.close();
    }

}
