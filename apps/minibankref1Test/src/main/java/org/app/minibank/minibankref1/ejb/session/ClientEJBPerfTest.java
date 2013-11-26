package org.app.minibank.minibankref1.ejb.session;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.BaseAction1;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;
import org.junit.Before;
import org.junit.Test;

public class ClientEJBPerfTest {

    private static final Logger log = Logger.getLogger(ClientEJBPerfTest.class);

    int nbloop = 100;

    @Before
    public void init() {
        Logger.getLogger("org.jboss.ejb.client.remoting").setLevel(Level.WARN); // otherwise too much logging !
    }

    @Test
    public void testPerfCluster1() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < nbloop; i++) {
            Context context = new InitialContext(TestUtil.createProperties1());
            Context jndiCtx = (Context) context.lookup("ejb:");
            Foo1Remote service = (Foo1Remote) jndiCtx.lookup(TestUtil.getJndi1());
            Object result = service.defaultTxPropagation(new BaseAction1().setDolog(false));
            if (i % 100 == 0) {
                log.info("" + i + ": " + result);
            }
            context.close();
            jndiCtx.close();
        }

        long end = System.currentTimeMillis();
        long totalTime = end - start;
        log.info("Total time: " + totalTime + " ms average: " + (totalTime / nbloop) + " ms.");
    }

    @Test
    public void testPerfCluster2() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < nbloop; i++) {
            Context context = new InitialContext(TestUtil.createProperties2());
            Context jndiCtx = (Context) context.lookup("ejb:");
            Foo2Remote service = (Foo2Remote) jndiCtx.lookup(TestUtil.getJndi2());
            Object result = service.defaultTxPropagation(new BaseAction2().setDolog(false));
            if (i % 100 == 0) log.info("" + i + ": " + result);
            context.close();
            jndiCtx.close();
        }
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        log.info("Total time: " + totalTime + " ms average: " + (totalTime / nbloop) + " ms.");
    }

    @Test
    public void testPerfCluster12() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < nbloop; i++) {
            Context context1 = new InitialContext(TestUtil.createProperties1());
            Context jndiCtx1 = (Context) context1.lookup("ejb:");
            Foo1Remote service = (Foo1Remote) jndiCtx1.lookup(TestUtil.getJndi1());

            CallRemote2UnsecuredAction1 action = new CallRemote2UnsecuredAction1();
            action.setRemoteLookupProperties(TestUtil.createProperties2());
            action.setJndi(TestUtil.getJndi2());
            action.setDolog(false);

            Object result = service.defaultTxPropagation(action);

            if (i % 100 == 0) log.info("" + i + ": " + result);
            context1.close();
            jndiCtx1.close();
        }

        long end = System.currentTimeMillis();
        long totalTime = end - start;
        log.info("Total time: " + totalTime + " ms average: " + (totalTime / nbloop) + " ms.");
    }
}
