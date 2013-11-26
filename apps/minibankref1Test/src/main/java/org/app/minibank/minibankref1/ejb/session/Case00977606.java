package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallRemote2UnsecuredAction1;
import org.app.minibank.minibankref1.action.CallRemote3UnsecuredAction1;
import org.app.minibank.minibankref1.action.SequenceAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Case00977606 {

    private static final Logger log = Logger.getLogger(Case00977606.class);

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
    public void call2Then3() throws Exception {
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

}
