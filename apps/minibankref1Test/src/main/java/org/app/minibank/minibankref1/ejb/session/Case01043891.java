package org.app.minibank.minibankref1.ejb.session;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.Case01043891Action;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Case01043891 {

    private static final Logger log = Logger.getLogger(Case01043891.class);

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

    @Test
    public void closeChannel() throws Exception {
        Object result = getFoo1Remote().defaultTxPropagation(new Case01043891Action());
        log.info("result = " + result);
    }

}
