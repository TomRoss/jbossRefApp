package org.app.minibank.minibankref2.ejb.session;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;
import org.junit.Test;

public class Client2EJBTest {

    private static final Logger log = Logger.getLogger(Client2EJBTest.class);

    @Test
    public void doDefaultAction() throws Exception {
        EAP6NamingContext ctx = new EAP6NamingContext(TestUtil.createProperties2());
        Foo2Remote service = (Foo2Remote) ctx.lookup(TestUtil.getJndi2());
        Object result = service.defaultTxPropagation(new BaseAction2());
        log.info(result);
        ctx.close();
    }

}
