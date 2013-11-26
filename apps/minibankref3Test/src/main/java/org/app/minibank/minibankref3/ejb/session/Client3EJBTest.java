package org.app.minibank.minibankref3.ejb.session;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref3.action.BaseAction3;
import org.app.minibank.minibankref3.ejb.session.Foo3Remote;
import org.junit.Test;

public class Client3EJBTest {

    private static final Logger log = Logger.getLogger(Client3EJBTest.class);

    @Test
    public void doDefaultAction() throws Exception {
        EAP6NamingContext ctx = new EAP6NamingContext(TestUtil.createProperties3());
        Foo3Remote service = (Foo3Remote) ctx.lookup(TestUtil.getJndi3());
        Object result = service.defaultTxPropagation(new BaseAction3());
        log.info(result);
        ctx.close();
    }

}
